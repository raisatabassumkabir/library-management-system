package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.service.DefaulterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    // AdminService is no longer used
    private final ManageBookRepository manageBookRepository;
    private final IssuedBookRepository issuedBookRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    private DefaulterService defaulterService;

    public AdminController(ManageBookRepository manageBookRepository,
            IssuedBookRepository issuedBookRepository, RegistrationRepository registrationRepository) {
        this.manageBookRepository = manageBookRepository;
        this.issuedBookRepository = issuedBookRepository;
        this.registrationRepository = registrationRepository;
    }

    // Manual login removed in favor of Spring Security
    // @PostMapping("/admin-login-form") was here

    @GetMapping("/adminDashboard")
    public String showDashboard(Model model) {
        long totalBooks = manageBookRepository.sumQuantities();
        long totalIssuedBooks = issuedBookRepository.count();
        long totalUser = registrationRepository.count();
        long totalDefaulters = defaulterService.findDefaulters().size();

        System.out.println("Total Books Count: " + totalBooks);
        System.out.println("Total Issued Books Count: " + totalIssuedBooks);
        System.out.println("Total Users Count: " + totalUser);
        System.out.println("Total Defaulters Count: " + totalDefaulters);

        model.addAttribute("totalBookCount", totalBooks);
        model.addAttribute("totalIssuedBooks", totalIssuedBooks);
        model.addAttribute("userCount", totalUser);
        model.addAttribute("defaulters", totalDefaulters);

        return "adminDashboard";
    }

}
