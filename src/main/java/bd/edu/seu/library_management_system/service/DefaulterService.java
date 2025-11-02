package bd.edu.seu.library_management_system.service;
import bd.edu.seu.library_management_system.model.Defaulter;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.repository.DefaulterRepository;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public DefaulterService(IssuedBookRepository issuedBookRepository,
                            DefaulterRepository defaulterRepository) {
        this.issuedBookRepository = issuedBookRepository;
        this.defaulterRepository = defaulterRepository;
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
                String key = book.getEmail() + "-" + book.getIsbn();

                // SKIP if fine was already PAID
                if (PAID_FINES.contains(key)) {
                    continue;
                }

                long daysLate = ChronoUnit.DAYS.between(book.getReturnDate(), today);
                long fine = daysLate * 10;

                Defaulter defaulter = new Defaulter(
                        book.getIssueDate(),
                        book.getEmail(),
                        fine,
                        book.getIsbn(),
                        book.getReturnDate(),
                        book.getTitle()
                );
                newDefaulters.add(defaulter);
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
//    public DefaulterService(IssuedBookRepository issuedBookRepository, DefaulterRepository defaulterRepository) {
//        this.issuedBookRepository = issuedBookRepository;
//
//        this.defaulterRepository = defaulterRepository;
//    }
//public List<Defaulter> findDefaulters() {
//    return defaulterRepository.findAll();
//}
//
//    @Transactional
//    public void updateDefaulters() {
//        List<IssuedBook> issuedBooks = issuedBookRepository.findAll();
//        LocalDate today = LocalDate.now();
//
//        // Step 1: Find which fines should exist
//        Set<String> shouldHaveFine = new HashSet<>(); // email + isbn
//        List<Defaulter> newDefaulters = new ArrayList<>();
//
//        for (IssuedBook book : issuedBooks) {
//            if (book.getReturnDate() != null && book.getReturnDate().isBefore(today)) {
//                String key = book.getEmail() + "-" + book.getIsbn();
//                shouldHaveFine.add(key);
//
//                long daysLate = ChronoUnit.DAYS.between(book.getReturnDate(), today);
//                long fine = daysLate * 10;
//
//                Defaulter defaulter = new Defaulter(
//                        book.getIssueDate(),
//                        book.getEmail(),
//                        fine,
//                        book.getIsbn(),
//                        book.getReturnDate(),
//                        book.getTitle()
//                );
//                newDefaulters.add(defaulter);
//            }
//        }
//
//        // Step 2: Delete fines that should NOT exist
//        List<Defaulter> current = defaulterRepository.findAll();
//        for (Defaulter d : current) {
//            String key = d.getEmail() + "-" + d.getIsbn();
//            if (!shouldHaveFine.contains(key)) {
//                defaulterRepository.delete(d); // Only delete if not overdue
//            }
//        }
//
//        // Step 3: Add new fines
//        defaulterRepository.saveAll(newDefaulters);
//    }
//    // New: Delete by ID
//    @Transactional
//    public void clearDefaulter(Long defaulterId) {
//        defaulterRepository.deleteById(defaulterId);
//    }

//    @Transactional
//    public void clearDefaulter(String email) {
//      defaulterRepository.deleteByEmail(email);
//    }

}

