package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public void saveRegistration(Registration registration) {
        String email = registration.getEmail();
        String userType = registration.getUserType();

        // 1. Domain Check
        if (email == null || !email.endsWith("@seu.edu.bd")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        // Extract part before @
        String localPart = email.substring(0, email.indexOf("@"));

        if ("student".equalsIgnoreCase(userType)) {
            // 2. Student Check: 13 digit integer
            if (!localPart.matches("\\d{13}")) {
                throw new IllegalArgumentException(
                        "Student email must be a 13-digit ID (e.g., 2022100000011@seu.edu.bd)");
            }
        } else if ("teacher".equalsIgnoreCase(userType)) {
            // 3. Teacher Check: Must contain a dot (.)
            if (!localPart.contains(".")) {
                throw new IllegalArgumentException(
                        "Teacher email must contain a dot (e.g., first.lastname@seu.edu.bd)");
            }
        }

        registrationRepository.save(registration);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
