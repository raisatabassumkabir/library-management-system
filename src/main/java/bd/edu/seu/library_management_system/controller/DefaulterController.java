package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.service.DefaulterService;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

public class DefaulterController {

    private final DefaulterService defaulterService;


    public DefaulterController( DefaulterService defaulterService) {
        this.defaulterService = defaulterService;
    }

    @GetMapping("/defaulterList")
    public String defaulterListPage(Model model) {
        model.addAttribute("defaulters",defaulterService.findDefaulters());
        return "defaulterList";
    }

    @PostMapping("/admin/clearDefaulter")
    public String clearDefaulter(@RequestParam String email, Model model) {
        defaulterService.clearDefaulter(email);
        model.addAttribute("defaulters",defaulterService.findDefaulters());
        return "defaulterList";
    }

}
