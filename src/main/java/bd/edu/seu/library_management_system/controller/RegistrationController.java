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
    public String registrationSubmit(@ModelAttribute Registration registration) {
        registrationService.saveRegistration(registration);
        return "index";

    }

}
