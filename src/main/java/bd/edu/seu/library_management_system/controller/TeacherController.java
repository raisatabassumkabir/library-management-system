package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.model.Defaulter;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.Teacher;
import bd.edu.seu.library_management_system.repository.DefaulterRepository;
import bd.edu.seu.library_management_system.repository.TeacherRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.service.DefaulterService;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import bd.edu.seu.library_management_system.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;
    private final IssuedBookService issuedBookService;
    private final DefaulterRepository defaulterRepository;
    private final DefaulterService defaulterService;
    private final RegistrationRepository registrationRepository;

    public TeacherController(TeacherService teacherService, TeacherRepository teacherRepository,
            IssuedBookService issuedBookService, DefaulterRepository defaulterRepository,
            DefaulterService defaulterService, RegistrationRepository registrationRepository) {
        this.teacherService = teacherService;
        this.teacherRepository = teacherRepository;
        this.issuedBookService = issuedBookService;
        this.defaulterRepository = defaulterRepository;
        this.defaulterService = defaulterService;
        this.registrationRepository = registrationRepository;
    }

    @PostMapping("/teacher-login-form")
    public String teacherLogin(@RequestParam String email, @RequestParam String password, Model model) {
        boolean isValid = teacherService.teacherLoginAuthentication(email, password);
        if (isValid) {
            return "redirect:/teacherDashboard?email=" + email;
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        }
    }

    @GetMapping("/teacherDashboard")
    public String teacherDashboard(@RequestParam String email, Model model) {
        email = email.trim();
        Optional<Teacher> teacherOptional = teacherRepository.findByEmail(email);

        // Update defaulters to ensure DB is consistent
        defaulterService.updateDefaulters();

        List<IssuedBook> borrowedBooks = issuedBookService.findIssuedBooksByEmail(email);
        List<Defaulter> defaulters = defaulterRepository.findByEmail(email);

        borrowedBooks.forEach(book -> {
            long fine = teacherService.calculateFine(book);
            book.setFine((double) fine);
        });

        if (teacherOptional.isPresent()) {
            model.addAttribute("teacher", teacherOptional.get());
        }

        // Fetch Registration info for Name
        Optional<bd.edu.seu.library_management_system.model.Registration> registrationOptional = registrationRepository
                .findById(email);
        if (registrationOptional.isPresent() && registrationOptional.get().getName() != null) {
            model.addAttribute("userName", registrationOptional.get().getName());
        } else {
            model.addAttribute("userName", "Teacher"); // Fallback
        }

        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("defaulters", defaulters);
        model.addAttribute("email", email);

        return "teacherDashboard";
    }
}
