package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.service.DefaulterService;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@SpringBootApplication
public class DefaulterController {

    private final IssuedBookService issuedBookService;
    private final DefaulterService defaulterService;


    public DefaulterController(IssuedBookService issuedBookService, DefaulterService defaulterService) {
        this.issuedBookService = issuedBookService;

        this.defaulterService = defaulterService;
    }

    @GetMapping("/defaulterList")
    public String defaulterListPage(Model model) {
        model.addAttribute("defaulters",defaulterService.findDefaulters());
        return "defaulterList";
    }

    @PostMapping("/admin/clearDefaulter")
    public String clearDefaulter(Model model) {
        model.addAttribute("defaulters",defaulterService.findDefaulters());
        return "defaulterList";
    }

}
