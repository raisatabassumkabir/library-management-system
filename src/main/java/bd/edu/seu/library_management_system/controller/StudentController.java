package bd.edu.seu.library_management_system.controller;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.Student;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.repository.StudentRepository;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import bd.edu.seu.library_management_system.service.RegistrationService;
import bd.edu.seu.library_management_system.service.StudentService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
@SpringBootApplication
public class StudentController
{
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationService registrationService;
    private final StudentService studentService;
    private final IssuedBookService issuedBookService;

    public StudentController(StudentRepository studentRepository, RegistrationRepository registrationRepository, RegistrationService registrationService, StudentService studentService, IssuedBookService issuedBookService) {
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
        this.registrationService = registrationService;
        this.studentService = studentService;
        this.issuedBookService = issuedBookService;
    }

    @PostMapping("/student-login-form")
    public String studentLogin(@RequestParam String email, @RequestParam String password, Model model) {
        boolean isValidEmailAndPasswordAndType = studentService.studentLoginAuthentication(email, password);

        if (isValidEmailAndPasswordAndType){
            return "redirect:/studentDashboard?email=" + email;
        }else {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        }
    }

    @GetMapping("/studentDashboard")
    public String studentDashboardPage(@RequestParam String email, Model model) {
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        List<IssuedBook> borrowedBooks = issuedBookService.findIssuedBooksByEmail(email);

        System.out.println("Borrowed books for " + email + ": " + borrowedBooks);


        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            model.addAttribute("borrowedBooks", borrowedBooks);
            model.addAttribute("email", email);

            return "studentDashboard";
        }
        return "studentDashboard";
    }
}
