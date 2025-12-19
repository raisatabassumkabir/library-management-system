package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewMembers {

    private final RegistrationService registrationService;

    public ViewMembers(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/viewMembers")
    public String viewMembersPage(Model model) {
        model.addAttribute("memberList", registrationService.getAllRegistrations());
        return "viewMembers";
    }
}
