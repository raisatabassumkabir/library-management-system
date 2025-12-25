package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Defaulter;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.repository.DefaulterRepository;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DefaulterService {

    private final IssuedBookRepository issuedBookRepository;
    private final DefaulterRepository defaulterRepository;

    // This remembers which fines were PAID (never come back)
    private static final Set<String> PAID_FINES = new HashSet<>();

    private final RegistrationRepository registrationRepository;

    public DefaulterService(IssuedBookRepository issuedBookRepository,
            DefaulterRepository defaulterRepository,
            RegistrationRepository registrationRepository) {
        this.issuedBookRepository = issuedBookRepository;
        this.defaulterRepository = defaulterRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<Defaulter> findDefaulters() {
        return defaulterRepository.findAll();
    }

    @Transactional
    public void updateDefaulters() {
        defaulterRepository.deleteAll(); // Start fresh

        List<IssuedBook> issuedBooks = issuedBookRepository.findAll();
        LocalDate today = LocalDate.now();

        List<Defaulter> newDefaulters = new ArrayList<>();

        for (IssuedBook book : issuedBooks) {
            if (book.getReturnDate() != null && book.getReturnDate().isBefore(today)) {
                String key = book.getEmail().trim() + "-" + book.getIsbn();

                // SKIP if fine was already PAID
                // TEMPORARILY DISABLED: User reported missing defaulters.
                // if (PAID_FINES.contains(key)) {
                // continue;
                // }

                // Check user type for fine calculation logic
                long fine = 0;
                long daysLate = ChronoUnit.DAYS.between(book.getReturnDate(), today);

                System.out.println("DEBUG: Book ISBN=" + book.getIsbn() + " Email=" + book.getEmail() + " ReturnDate="
                        + book.getReturnDate() + " DaysLate=" + daysLate);

                var registration = registrationRepository.findByEmail(book.getEmail());
                boolean isTeacher = registration.isPresent()
                        && "teacher".equalsIgnoreCase(registration.get().getUserType());

                System.out.println("DEBUG: User " + book.getEmail() + " isTeacher=" + isTeacher);

                if (isTeacher) {
                    // Teacher Logic: 3 days grace period
                    if (daysLate > 3) {
                        fine = (daysLate - 3) * 10;
                    } else {
                        System.out.println(
                                "DEBUG: Teacher " + book.getEmail() + " is within grace period. DaysLate=" + daysLate);
                        continue;
                    }
                } else {
                    // Student/Default Logic
                    fine = daysLate * 10;
                }

                System.out.println("DEBUG: Calculated fine=" + fine);

                if (fine > 0) {
                    Defaulter defaulter = new Defaulter(
                            book.getIssueDate(),
                            book.getEmail().trim(),
                            fine,
                            book.getIsbn(),
                            book.getReturnDate(),
                            book.getTitle());
                    newDefaulters.add(defaulter);
                    System.out.println("DEBUG: Added defaulter: " + defaulter.getEmail());
                } else {
                    System.out.println("DEBUG: Fine is 0, skipping defaulter list.");
                }
            }
        }

        defaulterRepository.saveAll(newDefaulters);
    }

    // CLEAR = FINE PAID → DELETE + REMEMBER
    @Transactional
    public void clearDefaulter(Long defaulterId) {
        Defaulter defaulter = defaulterRepository.findById(defaulterId)
                .orElseThrow(() -> new RuntimeException("Defaulter not found"));

        // Remember: this fine was PAID
        String key = defaulter.getEmail() + "-" + defaulter.getIsbn();
        PAID_FINES.add(key);

        // Delete from defaulter list
        defaulterRepository.delete(defaulter);
    }

}
