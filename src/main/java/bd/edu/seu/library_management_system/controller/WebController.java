package bd.edu.seu.library_management_system.controller;
import bd.edu.seu.library_management_system.model.Registration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@SpringBootApplication
public class WebController {
    @GetMapping("/home")
    public String homePage(){
        return "home";
    }
    @GetMapping("/index")
    public String indexPage(){
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }


//65789o0789878
    @GetMapping("/registration")
    public String registrationPage(Model model){
        model.addAttribute("registration", new Registration());
        return "registration";
    }




}
