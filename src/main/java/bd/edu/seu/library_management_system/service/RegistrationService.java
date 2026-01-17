package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public RegistrationService(RegistrationRepository registrationRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.registrationRepository = registrationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveRegistration(Registration registration) {
        String email = registration.getEmail();
        String userType = registration.getUserType();
        String name = registration.getName();

        // 0. Name Check
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        System.out.println("DEBUG: Saving registration for email: " + email + " Name: " + name);

        // 1. Domain Check
        if (email == null || !email.endsWith("@seu.edu.bd")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        // Extract part before @
        String localPart = email.substring(0, email.indexOf("@"));

        if ("student".equalsIgnoreCase(userType)) {
            // 2. Student Check: 13 digit integer
            if (!localPart.matches("\\d{13}")) {
                throw new IllegalArgumentException("Student email is incorrect");
            }
        } else if ("teacher".equalsIgnoreCase(userType)) {
            // 3. Teacher Check: Must contain a dot (.)
            if (!localPart.contains(".")) {
                throw new IllegalArgumentException("Teacher email is incorrect");
            }
        }

        // Encode password before saving
        registration.setPassword(passwordEncoder.encode(registration.getPassword()));
        registrationRepository.save(registration);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
