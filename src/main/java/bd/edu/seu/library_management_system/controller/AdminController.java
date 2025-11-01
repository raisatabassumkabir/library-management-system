package bd.edu.seu.library_management_system.controller;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.service.AdminService;
import bd.edu.seu.library_management_system.service.DefaulterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@SpringBootApplication
public class AdminController {
    @Autowired
    private final AdminService adminService;
    private final ManageBookRepository manageBookRepository;
    private final IssuedBookRepository issuedBookRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    private DefaulterService defaulterService;



    public AdminController(AdminService adminService, ManageBookRepository manageBookRepository, IssuedBookRepository issuedBookRepository, RegistrationRepository registrationRepository) {
        this.adminService = adminService;
        this.manageBookRepository = manageBookRepository;
        this.issuedBookRepository = issuedBookRepository;
        this.registrationRepository = registrationRepository;
    }


    @PostMapping("/admin-login-form")
       public String adminAuthentication(@RequestParam String email, @RequestParam String password, Model model){


       boolean isValidEmailAndPassword=adminService.adminAuthenticationByEmailAndPassword(email, password);
       if (isValidEmailAndPassword){
           return "redirect:/adminDashboard";
       }

        model.addAttribute("error","Invalid email or password");
        return "admin";
   }

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
