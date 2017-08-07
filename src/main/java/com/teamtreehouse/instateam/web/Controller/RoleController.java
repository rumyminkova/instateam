package com.teamtreehouse.instateam.web.Controller;

import com.teamtreehouse.instateam.model.Role;
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

/**
 * Created by Rumy on 7/17/2017.
 */
@Controller
public class RoleController {
  @Autowired
  private RoleService roleService;

  @RequestMapping(value="/roles")
   public String listRoles(Model model) {
      // Get all roles
        List<Role> roles =roleService.findAll() ;
        model.addAttribute("roles",roles);
        return "role/index";
   }


    // Add a role
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role",result);

            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute("role",role);

            // Redirect back to the form
            return "redirect:/roles/add";
        }

        roleService.save(role);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role successfully added!", FlashMessage.Status.SUCCESS));

        //  Redirect browser to /roles
        return "redirect:/roles";
    }


    @RequestMapping("roles/add")
    public String formNewRole(Model model) {
        //  Add model attributes needed for new form
        if(!model.containsAttribute("role")) {
            model.addAttribute("role",new Role());
        }
        model.addAttribute("action","/roles");
        model.addAttribute("heading","New Role");
        model.addAttribute("submit","Add");

        return "role/form";
    }


    // Form for editing an existing category
    @RequestMapping("roles/{roleId}/edit")
    public String formEditRole(@PathVariable Long roleId, Model model) {
        // TODO: Add model attributes needed for new form
        if(!model.containsAttribute("role")) {
            model.addAttribute("role",roleService.findById(roleId));
        }
        model.addAttribute("action",String.format("/roles/%s",roleId));
        model.addAttribute("heading","Edit Role");
        model.addAttribute("submit","Update");

        return "role/form";
    }

    // Update an existing role
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.POST)
    public String updateRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes) {
        // TODO: Update role if valid data was received
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role",result);

            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute("role",role);

            // Redirect back to the form
            return String.format("redirect:/roles/%s/edit",role.getId());
        }

        roleService.save(role);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role successfully updated!", FlashMessage.Status.SUCCESS));

        // TODO: Redirect browser to /roles
        return "redirect:/roles";
    }



    // Delete an existing role
    @RequestMapping(value = "/roles/{roleId}/delete", method = RequestMethod.POST)
    public String deleteRole(@PathVariable Long roleId, RedirectAttributes redirectAttributes) {
        Role rl = roleService.findById(roleId);

        // TODO: Delete role
       /* if(rl.getGifs().size() > 0) {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Only empty categories can be deleted.", FlashMessage.Status.FAILURE));
            return String.format("redirect:/categories/%s/edit",categoryId);
        }*/
        roleService.delete(rl);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role deleted!", FlashMessage.Status.SUCCESS));

        return "redirect:/roles";
    }

}
