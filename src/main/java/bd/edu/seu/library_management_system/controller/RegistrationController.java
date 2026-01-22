package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;

    }

    @PostMapping("/registration-form")
    public String registrationSubmit(@ModelAttribute Registration registration, org.springframework.ui.Model model) {
        try {
            registrationService.saveRegistration(registration);
            model.addAttribute("registration", new Registration()); // Required for index.html login forms
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registration", registration); // keep input
            return "registration";
        }
    }

    // Admin Add Member Page
    @org.springframework.web.bind.annotation.GetMapping("/admin/add-member")
    public String showAddMemberForm(org.springframework.ui.Model model) {
        Registration registration = new Registration();
        registration.setUserType(""); // Set default to empty to match placeholder option
        model.addAttribute("registration", registration);
        return "addMember";
    }

    // Admin Add Member Submission
    @PostMapping("/admin/add-member/save")
    public String adminAddMember(@ModelAttribute Registration registration, org.springframework.ui.Model model) {
        try {
            registrationService.saveRegistration(registration);
            model.addAttribute("success", "Member added successfully!");
            model.addAttribute("registration", new Registration()); // Reset form
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registration", registration); // Keep input
        }
        return "addMember";
    }

}
