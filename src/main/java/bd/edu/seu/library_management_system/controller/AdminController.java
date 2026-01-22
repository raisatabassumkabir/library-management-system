package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.service.DefaulterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bd.edu.seu.library_management_system.repository.ReturnBookRepository;

@Controller
public class AdminController {
    // AdminService is no longer used
    private final ManageBookRepository manageBookRepository;
    private final IssuedBookRepository issuedBookRepository;
    private final RegistrationRepository registrationRepository;
    private final ReturnBookRepository returnBookRepository;

    @Autowired
    private DefaulterService defaulterService;

    public AdminController(ManageBookRepository manageBookRepository,
            IssuedBookRepository issuedBookRepository,
            RegistrationRepository registrationRepository,
            ReturnBookRepository returnBookRepository) {
        this.manageBookRepository = manageBookRepository;
        this.issuedBookRepository = issuedBookRepository;
        this.registrationRepository = registrationRepository;
        this.returnBookRepository = returnBookRepository;
    }

    // Manual login removed in favor of Spring Security
    // @PostMapping("/admin-login-form") was here

    @GetMapping("/adminDashboard")
    public String showDashboard(Model model) {
        long totalBooks = manageBookRepository.sumQuantities();
        long totalIssuedBooks = issuedBookRepository.count();
        long totalUser = registrationRepository.count();
        long totalDefaulters = defaulterService.findDefaulters().size();
        long totalReturnedBooks = returnBookRepository.count();

        long uniqueBookCount = manageBookRepository.count();

        System.out.println("Total Books Count (Copies): " + totalBooks);
        System.out.println("Unique Book Titles: " + uniqueBookCount);
        System.out.println("Total Issued Books Count: " + totalIssuedBooks);
        System.out.println("Total Users Count: " + totalUser);
        System.out.println("Total Defaulters Count: " + totalDefaulters);
        System.out.println("Total Returned Books Count: " + totalReturnedBooks);

        model.addAttribute("totalBookCount", totalBooks);
        model.addAttribute("uniqueBookCount", uniqueBookCount);
        model.addAttribute("totalIssuedBooks", totalIssuedBooks);
        model.addAttribute("userCount", totalUser);
        model.addAttribute("defaulters", totalDefaulters);
        model.addAttribute("returnedBooks", totalReturnedBooks);

        return "adminDashboard";
    }

}
