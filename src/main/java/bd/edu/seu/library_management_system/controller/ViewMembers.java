package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.service.RegistrationService;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String viewMembersPage(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("memberList", registrationService.searchRegistrations(query));
        } else {
            model.addAttribute("memberList", registrationService.getAllRegistrations());
        }
        return "viewMembers";
    }

    @GetMapping("/admin/members/search-fragment")
    public String searchMembersFragment(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("memberList", registrationService.searchRegistrations(query));
        } else {
            model.addAttribute("memberList", registrationService.getAllRegistrations());
        }
        return "viewMembers :: memberTableBody";
    }

}
