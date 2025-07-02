package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.service.IssuedBookService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SpringBootApplication
public class DefaulterController {

    private final IssuedBookService issuedBookService;

    public DefaulterController(IssuedBookService issuedBookService) {
        this.issuedBookService = issuedBookService;
    }

    @GetMapping("/defaulterList")
    public String defaulterListPage(Model model) {
        model.addAttribute("defaulters", issuedBookService.findDefaulters());
        return "defaulterList";
    }


}
