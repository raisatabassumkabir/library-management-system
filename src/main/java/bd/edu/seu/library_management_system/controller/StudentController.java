package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.model.Defaulter;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.Student;
import bd.edu.seu.library_management_system.repository.DefaulterRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.repository.StudentRepository;
import bd.edu.seu.library_management_system.service.DefaulterService;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import bd.edu.seu.library_management_system.service.RegistrationService;
import bd.edu.seu.library_management_system.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationService registrationService;
    private final StudentService studentService;
    private final IssuedBookService issuedBookService;
    private final DefaulterRepository defaulterRepository;
    private final DefaulterService defaulterService;

    public StudentController(StudentRepository studentRepository, RegistrationRepository registrationRepository,
            RegistrationService registrationService, StudentService studentService,
            IssuedBookService issuedBookService,
            DefaulterRepository defaulterRepository,
            DefaulterService defaulterService) {
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
        this.registrationService = registrationService;
        this.studentService = studentService;
        this.issuedBookService = issuedBookService;
        this.defaulterRepository = defaulterRepository;
        this.defaulterService = defaulterService;
    }

    @PostMapping("/student-login-form")
    public String studentLogin(@RequestParam String email, @RequestParam String password, Model model) {
        boolean isValidEmailAndPasswordAndType = studentService.studentLoginAuthentication(email, password);

        if (isValidEmailAndPasswordAndType) {
            return "redirect:/studentDashboard?email=" + email;
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        }
    }

    @GetMapping("/studentDashboard")
    public String studentDashboardPage(@RequestParam String email, Model model) {
        email = email.trim(); // Ensure no leading/trailing spaces
        Optional<Student> studentOptional = studentRepository.findByEmail(email);

        // Refresh defaulter list to ensure accuracy
        defaulterService.updateDefaulters();

        List<IssuedBook> borrowedBooks = issuedBookService.findIssuedBooksByEmail(email);
        List<Defaulter> defaulters = defaulterRepository.findByEmail(email);

        System.out.println("DEBUG: Dashboard requested for email: '" + email + "'");
        System.out.println("DEBUG: Found " + borrowedBooks.size() + " borrowed books.");
        System.out.println("DEBUG: Found " + defaulters.size() + " defaulter records.");

        borrowedBooks.forEach(book -> {
            // Use direct calculation as requested by user (via added service method)
            long calculatedFine = issuedBookService.calculateFine(book);
            book.setFine((double) calculatedFine);
        });

        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
        }

        // ALWAYS add these to the model so the dashboard works even if student record
        // is slightly mismatched
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("email", email);

        return "studentDashboard";
    }
}
