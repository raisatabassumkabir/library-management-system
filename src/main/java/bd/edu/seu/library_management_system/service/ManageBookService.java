package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManageBookService {
    private final ManageBookRepository manageBookRepository;
    private final IssuedBookRepository issuedBookRepository;

    public ManageBookService(ManageBookRepository manageBookRepository, IssuedBookRepository issuedBookRepository) {
        this.manageBookRepository = manageBookRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    public void manageBook(ManageBook manageBook) {
        // When adding a new book, Total Quantity is the input.
        // Remaining Quantity (remainingQuantity) should be initialized to Total
        // Quantity.
        manageBook.setRemainingQuantity(manageBook.getTotalQuantity());
        manageBookRepository.save(manageBook);
    }

    public ManageBook getBookByIsbn(int isbn) {
        return manageBookRepository.findByIsbn(isbn).orElse(null);
    }

    public List<ManageBook> getAllBooks() {
        return manageBookRepository.findAll();
    }

    public ManageBook updateBook(int isbn, ManageBook manageUpdateBook) {
        Optional<ManageBook> optionalManageBook = manageBookRepository.findById(manageUpdateBook.getIsbn());
        if (optionalManageBook.isPresent()) {
            ManageBook existingBook = optionalManageBook.get();
            existingBook.setTitle(manageUpdateBook.getTitle());
            existingBook.setAuthor(manageUpdateBook.getAuthor());

            // Calculate difference in Total Quantity to adjust Remaining Quantity
            int oldTotal = existingBook.getTotalQuantity();
            int newTotal = manageUpdateBook.getTotalQuantity();
            int diff = newTotal - oldTotal;

            existingBook.setTotalQuantity(newTotal);
            existingBook.setRemainingQuantity(existingBook.getRemainingQuantity() + diff);

            return manageBookRepository.save(existingBook);
        }
        return null;
    }

    public void deleteBook(int isbn) {

        manageBookRepository.deleteById(isbn);
    }

    public List<ManageBook> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }

        String searchTerm = query.trim();
        List<ManageBook> results = new ArrayList<>();

        // Check if query is numeric (for ISBN search)
        if (searchTerm.matches("\\d+")) {
            try {
                int isbn = Integer.parseInt(searchTerm);
                Optional<ManageBook> book = manageBookRepository.findByIsbn(isbn);
                book.ifPresent(results::add);
            } catch (NumberFormatException e) {
                // Ignore, proceed to other searches
            }
        }

        // Search by Title or writer
        results.addAll(manageBookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(searchTerm,
                searchTerm));

        return results;
    }

    //  Initialization Fix
    // method runs once on startup to fix  book data
    @jakarta.annotation.PostConstruct
    public void fixTotalQuantities() {
        List<ManageBook> allBooks = manageBookRepository.findAll();
        boolean needsUpdate = false;
        System.out.println("Starting Data Migration Check...");

        for (ManageBook book : allBooks) {
            long borrowedCount = issuedBookRepository.countByIsbn(book.getIsbn());


            int currentTotal = book.getTotalQuantity();
            int currentRemaining = book.getRemainingQuantity();
            int expectedTotal = currentRemaining + (int) borrowedCount;

            System.out.printf("Book ISBN %d: Remaining=%d, Issued=%d, Current Total=%d, Expected Total=%d%n",
                    book.getIsbn(), currentRemaining, borrowedCount, currentTotal, expectedTotal);

            if (currentTotal == 0 || (currentTotal < expectedTotal)) {
                System.out.printf(" >> UPDATING ISBN %d: Setting Total from %d to %d%n", book.getIsbn(), currentTotal,
                        expectedTotal);
                book.setTotalQuantity(expectedTotal);
                manageBookRepository.save(book);
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            System.out.println("Migrated existing books: Corrected Total Quantity = Remaining + Borrowed.");
        } else {
            System.out
                    .println("No migration needed: All Total Quantities are correct (Total >= Remaining + Borrowed).");
        }
    }

}
