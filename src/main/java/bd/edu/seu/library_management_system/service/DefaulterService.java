package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Defaulter;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.repository.DefaulterRepository;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import java.util.Optional;
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
    private final ManageBookRepository manageBookRepository;
    private final RegistrationRepository registrationRepository;

    public DefaulterService(IssuedBookRepository issuedBookRepository,
            DefaulterRepository defaulterRepository,
            ManageBookRepository manageBookRepository,
            RegistrationRepository registrationRepository) {
        this.issuedBookRepository = issuedBookRepository;
        this.defaulterRepository = defaulterRepository;
        this.manageBookRepository = manageBookRepository;
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

                // Check user type for fine calculation
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
                    // Student/Default
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

    @Transactional
    public void clearDefaulter(Long defaulterId) {
        Defaulter defaulter = defaulterRepository.findById(defaulterId)
                .orElseThrow(() -> new RuntimeException("Defaulter not found"));

        System.out
                .println("DEBUG: Clearing All Issues for: " + defaulter.getEmail() + ", ISBN: " + defaulter.getIsbn());

        Set<IssuedBook> booksToDelete = new HashSet<>();
        String targetEmail = defaulter.getEmail().trim();

        // 1. Find by ISBN
        List<IssuedBook> booksByIsbn = issuedBookRepository.findByIsbn(defaulter.getIsbn());
        for (IssuedBook book : booksByIsbn) {
            if (book.getEmail() != null && book.getEmail().trim().equalsIgnoreCase(targetEmail)) {
                booksToDelete.add(book);
            }
        }

        // 2. Find by Email
        List<IssuedBook> booksByEmail = issuedBookRepository.findByEmail(targetEmail);
        for (IssuedBook book : booksByEmail) {
            if (book.getIsbn() == defaulter.getIsbn()) {
                booksToDelete.add(book);
            }
        }

        if (booksToDelete.isEmpty()) {
            System.err
                    .println("SEVERE ERROR: Found NO IssuedBooks for " + targetEmail + " ISBN " + defaulter.getIsbn());

        } else {
            System.out
                    .println("DEBUG: Found " + booksToDelete.size() + " records to delete. Proceeding with deletion.");
            // We delete ALL matches regardless of date. If they are in the Defaulter list,

            issuedBookRepository.deleteAll(booksToDelete);
        }

        // 2. Restock Inventory
        if (!booksToDelete.isEmpty()) {
            Optional<ManageBook> manageBookOptional = manageBookRepository.findByIsbn(defaulter.getIsbn());
            if (manageBookOptional.isPresent()) {
                ManageBook book = manageBookOptional.get();
                // Add back as many as we deleted
                book.setRemainingQuantity(book.getRemainingQuantity() + booksToDelete.size());
                manageBookRepository.save(book);
                System.out.println("DEBUG: Restocked inventory by " + booksToDelete.size());
            }
        }

        // 3. Delete Defaulter Record
        defaulterRepository.delete(defaulter);
    }

}
