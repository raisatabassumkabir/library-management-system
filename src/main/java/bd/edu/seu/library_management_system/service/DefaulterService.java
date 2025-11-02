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
import java.util.List;

@Service
public class DefaulterService {

    private final IssuedBookRepository issuedBookRepository;
    private final DefaulterRepository defaulterRepository;


    public DefaulterService(IssuedBookRepository issuedBookRepository, DefaulterRepository defaulterRepository) {
        this.issuedBookRepository = issuedBookRepository;

        this.defaulterRepository = defaulterRepository;
    }
public List<Defaulter> findDefaulters() {
    return defaulterRepository.findAll();
}

    // Populate defaulters table (call manually or via scheduler)
    @Transactional
    public void updateDefaulters() {
        defaulterRepository.deleteAll(); // Start fresh

        List<IssuedBook> issuedBooks = issuedBookRepository.findAll();
        List<Defaulter> newDefaulters = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (IssuedBook book : issuedBooks) {
            if (book.getReturnDate() != null && book.getReturnDate().isBefore(today)) {
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
    @Transactional
    public void clearDefaulter(String email) {
      defaulterRepository.deleteByEmail(email);
    }

}

