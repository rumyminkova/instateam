package com.teamtreehouse.instateam.web.Controller;

import com.teamtreehouse.instateam.model.Collaborator;
import com.teamtreehouse.instateam.model.Project;
import com.teamtreehouse.instateam.model.Role;
import com.teamtreehouse.instateam.service.CollaboratorService;
import com.teamtreehouse.instateam.service.ProjectService;
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
import java.util.List;
import java.util.Set;

/**
 * Created by Rumy on 7/24/2017.
 */
@Controller
public class CollaboratorController {
  @Autowired
  private CollaboratorService collaboratorService;

  @Autowired
  private RoleService roleService;

    @Autowired
    private ProjectService projectService;


  @RequestMapping("/collaborators")
  public String listCollaborators(Model model) {
     // Get all
    List<Collaborator> collaborators =collaboratorService.findAll() ;
    model.addAttribute("collaborators",collaborators);
    return "collaborator/index";
  }

    // Add a collaborator
    @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
    public String addCollaborator(@Valid Collaborator collaborator, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.collaborator",result);

            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute("collaborator",collaborator);

            // Redirect back to the form
            return "redirect:/collaborators/add";
        }

        collaboratorService.save(collaborator);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Collaborator successfully added!", FlashMessage.Status.SUCCESS));

        // Redirect browser
        return "redirect:/collaborators";
    }


    @RequestMapping("collaborators/add")
    public String formNewCollaborator(Model model) {
        // Add model attributes needed for new form
        if(!model.containsAttribute("collaborator")) {
            model.addAttribute("collaborator",new Collaborator());
        }
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("action","/collaborators");
        model.addAttribute("heading","New Collaborator");
        model.addAttribute("submit","Add");

        return "collaborator/form";
    }


    // Form for editing
    @RequestMapping("collaborators/{collaboratorId}/edit")
    public String formEditCollaborator(@PathVariable Long collaboratorId, Model model) {
        if(!model.containsAttribute("collaborator")) {
            model.addAttribute("collaborator",collaboratorService.findById(collaboratorId));
        }
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("action",String.format("/collaborators/%s",collaboratorId));
        model.addAttribute("heading","Edit Collaborator");
        model.addAttribute("submit","Update");
        return "collaborator/form";
    }

    // Update an existing collaborator
    @RequestMapping(value = "/collaborators/{collaboratorId}", method = RequestMethod.POST)
    public String updateCollaborator(@Valid Collaborator collaborator, BindingResult result, RedirectAttributes redirectAttributes) {
        // Update  if valid data was received
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.collaborator",result);
           // Add  if invalid was received
            redirectAttributes.addFlashAttribute("collaborator",collaborator);
           // Redirect back to the form
            return String.format("redirect:/collaborators/%s/edit",collaborator.getId());
        }

         collaboratorService.save(collaborator);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Collaborator successfully updated!", FlashMessage.Status.SUCCESS));

        return "redirect:/collaborators";
    }


}
