package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.service.DefaulterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DefaulterController {

    private final DefaulterService defaulterService;

    public DefaulterController(DefaulterService defaulterService) {
        this.defaulterService = defaulterService;

    }

    @GetMapping("/defaulterList")
    public String defaulterListPage(@RequestParam(required = false) String query, Model model) {
        defaulterService.updateDefaulters(); // Refreshes list
        if (query != null && !query.isEmpty()) {
            model.addAttribute("defaulters", defaulterService.searchDefaulters(query));
        } else {
            model.addAttribute("defaulters", defaulterService.findDefaulters());
        }
        return "defaulterList";
    }

    @PostMapping("/admin/clearDefaulter")
    public String clearDefaulter(@RequestParam Long defaulterId, RedirectAttributes ra) {
        defaulterService.clearDefaulter(defaulterId);
        ra.addFlashAttribute("msg", "Defaulter record cleared successfully!");
        return "redirect:/defaulterList";
    }

}
