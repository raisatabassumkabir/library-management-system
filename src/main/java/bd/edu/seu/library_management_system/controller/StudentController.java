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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationService registrationService;
    // StudentService logic moved to secure login flow
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
        // this.studentService = studentService; // Kept via dependency injection but
        // field is unused - to be safe removing usage
        this.issuedBookService = issuedBookService;
        this.defaulterRepository = defaulterRepository;
        this.defaulterService = defaulterService;
    }

    // Manual login removed in favor of Spring Security

    @GetMapping("/studentDashboard")
    public String studentDashboardPage(java.security.Principal principal, Model model) {
        String email = principal.getName();
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

        // Fetch Registration info for Name
        Optional<bd.edu.seu.library_management_system.model.Registration> registrationOptional = registrationRepository
                .findById(email);
        if (registrationOptional.isPresent() && registrationOptional.get().getName() != null) {
            model.addAttribute("userName", registrationOptional.get().getName());
        } else {
            model.addAttribute("userName", "Student"); // Fallback
        }

        // ALWAYS add these to the model so the dashboard works even if student record
        // is slightly mismatched
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("defaulters", defaulters);
        model.addAttribute("email", email);

        return "studentDashboard";
    }
}
