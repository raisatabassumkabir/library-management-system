package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final RegistrationRepository registrationRepository;

    public TeacherService(TeacherRepository teacherRepository, RegistrationRepository registrationRepository) {
        this.teacherRepository = teacherRepository;
        this.registrationRepository = registrationRepository;
    }

    public boolean teacherLoginAuthentication(String email, String password) {
        Optional<Registration> registrationOptional = registrationRepository.findByEmail(email);

        if (registrationOptional.isPresent()) {
            Registration registration = registrationOptional.get();
            if (registration.getEmail().equalsIgnoreCase(email.trim())
                    && registration.getPassword().equals(password)
                    && registration.getUserType().equalsIgnoreCase("teacher")) {
                return true;
            }
        }
        return false;
    }

    public long calculateFine(IssuedBook issuedBook) {
        if (issuedBook.getReturnDate() != null) {
            LocalDate today = LocalDate.now();
            if (issuedBook.getReturnDate().isBefore(today)) {
                long daysLate = ChronoUnit.DAYS.between(issuedBook.getReturnDate(), today);

                // Warning for an additional three days
                if (daysLate <= 3) {
                    return 0; // In warning period
                } else {
                    // Charged a fine as the students get (10 per day)
                    // Applying fine for days exceeding the 3-day warning period
                    return (daysLate - 3) * 10;
                }
            }
        }
        return 0;
    }
}
