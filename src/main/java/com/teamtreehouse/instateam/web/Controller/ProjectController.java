package com.teamtreehouse.instateam.web.Controller;

import com.teamtreehouse.instateam.model.Collaborator;
import com.teamtreehouse.instateam.model.Project;
import com.teamtreehouse.instateam.model.Role;
import com.teamtreehouse.instateam.model.StatusRepository;
import com.teamtreehouse.instateam.service.CollaboratorService;
import com.teamtreehouse.instateam.service.ProjectService;
import com.teamtreehouse.instateam.service.ProjectServiceImpl;
import com.teamtreehouse.instateam.service.RoleService;
import com.teamtreehouse.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rumy on 7/24/2017.
 */
@Controller
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StatusRepository projectStatus;


    // Get all
    @RequestMapping("/")
    public String listProjects(Model model) {
        List<Project> projects =projectService.findAll() ;
        model.addAttribute("projects",projects);
        return "project/index";
    }


    // Project details page
    @RequestMapping( "projects/{projectId}/details")
      public String projectDetails(@PathVariable Long projectId, Model model) {
         Project project = projectService.findById(projectId);
         Map<Role,Collaborator> rolesCollaboratorsMap=createRolesCollaboratorsMap(project);
         model.addAttribute("project", project);
         model.addAttribute("rolesCollaboratorsMap", rolesCollaboratorsMap);
         model.addAttribute("roles", roleService.findAll());
       return "project/details";
    }


    // Add new project
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String addProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project",result);

            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute("project",project);

            // Redirect back to the form
            return "redirect:/projects/add";
        }

        projectService.save(project);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project successfully added!", FlashMessage.Status.SUCCESS));

        // Redirect browser
        return String.format("redirect:/projects/%s/details",project.getProject_id());
    }


    @RequestMapping("/projects/add")
    public String formNewProject(Model model) {
        if(!model.containsAttribute("project")) {
            model.addAttribute("project",new Project());
        }
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("status",projectStatus.getStatus());
        model.addAttribute("action","/");

        return "project/form";
    }

    // Form for editing
    @RequestMapping("projects/{projectId}/edit")
    public String formEditProject(@PathVariable Long projectId, Model model) {
        if(!model.containsAttribute("project")) {
            model.addAttribute("project",projectService.findById(projectId));
        }
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("status",projectStatus.getStatus());
        model.addAttribute("action",String.format("/projects/%s",projectId));
        model.addAttribute("submit","Update");
        return "project/form";
    }

    // Update an existing project
    @RequestMapping(value = "projects/{projectId}", method = RequestMethod.POST)
    public String updateProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        // Update  if valid data was received
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project",result);

            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute("project",project);

            // Redirect back to the form
            return String.format("redirect:/projects/%s/edit",project.getProject_id());
        }

        Project sourceProject= projectService.findById(project.getProject_id());
        Set<Role> rolesTemp=project.getRolesNeeded();
        Set<Collaborator> collaboratorsTemp=sourceProject.getCollaborators();


        for (Role role :rolesTemp
                ){
            for (Collaborator cl: collaboratorsTemp
                 ) {
                if (cl.getRole().equals(role)){
                    project.addCollaborator(cl);
                }
            }
        }
        projectService.save(project);

         redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project successfully updated!", FlashMessage.Status.SUCCESS));
          return String.format("redirect:/projects/%s/details",project.getProject_id());
    }


    // Form for editing collaborators
    @RequestMapping("projects/{projectId}/collaborators")
    public String formEditProjectCollaborators(@PathVariable Long projectId, Model model) {
        if(!model.containsAttribute("project")) {
            model.addAttribute("project",projectService.findById(projectId));
        }
        model.addAttribute("action",String.format("/projects/%s/collaborators-update",projectId));
        return "project/edit-collaborators";
    }


    // Update an collaborators
    @RequestMapping(value = "projects/{projectId}/collaborators-update", method = RequestMethod.POST)
    public String updateProjectCollaborators(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        // Update  if valid data was received
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project",result);
            redirectAttributes.addFlashAttribute("project",project);
          // Redirect back to the form
            return String.format("redirect:/projects/%s/collaborators",project.getProject_id());
        }

        Project projectToUpdate= projectService.findById(project.getProject_id());
        projectToUpdate.setCollaborators(project.getCollaborators());
        projectService.save(projectToUpdate);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project's collaborators successfully updated!", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/projects/%s/details",project.getProject_id());
    }



    public Map<Role,Collaborator> createRolesCollaboratorsMap(Project project){
        Map<Role,Collaborator> rolesCollaboratorsMap=new HashMap<>();
        Set<Role> roles=project.getRolesNeeded();
        Set<Collaborator> collaborators=project.getCollaborators();
        for (Role role:roles
                ) {
            Collaborator collaborator=collaborators.stream()
                    .filter(cl->(cl.getRole()).equals(role))
                    .findAny().orElse(null);

            rolesCollaboratorsMap.put(role,collaborator);

        }
        return rolesCollaboratorsMap;
    }


}
