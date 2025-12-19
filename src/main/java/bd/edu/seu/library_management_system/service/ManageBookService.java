package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import org.springframework.stereotype.Service;

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

    // Data Migration / Initialization Fix
    // This method runs once on startup to fix or verify book data
    @jakarta.annotation.PostConstruct
    public void fixTotalQuantities() {
        List<ManageBook> allBooks = manageBookRepository.findAll();
        boolean needsUpdate = false;
        System.out.println("Starting Data Migration Check...");

        for (ManageBook book : allBooks) {
            long borrowedCount = issuedBookRepository.countByIsbn(book.getIsbn());
            // Expected Total should effectively be: what's on the shelf (remaining) +
            // what's out (borrowed).
            // NOTE: The 'Total Quantity' is the master record of Inventory. 'Remaining'
            // changes.
            // However, since we are correcting data, we assume 'Total' might be wrong if it
            // doesn't account for issued books.

            // Case 1: Total is 0 (Legacy uninitialized).
            // Case 2: Total == Remaining, but there ARE borrowed books. This means Total is
            // under-counted.
            // Example: Total 7, Remaining 7, Borrowed 1. Real Total should be 8.

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
