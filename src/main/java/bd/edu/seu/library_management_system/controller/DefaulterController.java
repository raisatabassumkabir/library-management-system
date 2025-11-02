package bd.edu.seu.library_management_system.controller;
import bd.edu.seu.library_management_system.repository.DefaulterRepository;
import bd.edu.seu.library_management_system.service.DefaulterService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@SpringBootApplication

public class DefaulterController {

    private final DefaulterService defaulterService;



    public DefaulterController(DefaulterService defaulterService) {
        this.defaulterService = defaulterService;

    }

    @GetMapping("/defaulterList")
    public String defaulterListPage( Model model) {
        defaulterService.updateDefaulters();
        model.addAttribute("defaulters",defaulterService.findDefaulters());
        return "defaulterList";
    }

    @PostMapping("/admin/clearDefaulter")
    public String clearDefaulter(@RequestParam String email, RedirectAttributes ra) {

        defaulterService.clearDefaulter(email);
        ra.addFlashAttribute("msg", "Defaulter cleared and list refreshed!");
        return "redirect:/defaulterList";
    }


}
