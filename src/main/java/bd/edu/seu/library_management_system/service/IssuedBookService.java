package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Defaulter;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IssuedBookService {
    private final IssuedBookRepository issuedBookRepository;
    private final ManageBookRepository manageBookRepository;
    private final RegistrationRepository registrationRepository;

    public IssuedBookService(IssuedBookRepository issuedBookRepository, ManageBookRepository manageBookRepository, RegistrationRepository registrationRepository) {
        this.issuedBookRepository = issuedBookRepository;
        this.manageBookRepository = manageBookRepository;
        this.registrationRepository = registrationRepository;
    }
    public String saveIssuedBook(IssuedBook issuedBook) {
       //check registration
        Optional<Registration> registeredUser = registrationRepository.findByEmail(issuedBook.getEmail());

        if (registeredUser.isEmpty()) {
            System.out.println("Email not registered: " + issuedBook.getEmail());
            return "Email not registered. Please register first.";
        }


        Optional<ManageBook> optionalManageBook = manageBookRepository.findByIsbn(issuedBook.getIsbn());

        if (optionalManageBook.isPresent()) {
            ManageBook manageBook = optionalManageBook.get();

            if (!manageBook.getTitle().equalsIgnoreCase(issuedBook.getTitle())) {
                return "Title do not matches with the ISBN.";
            }

             if (manageBook.getQuantity() <= 0) {
                return "Book is currently out of stock.";
            }

            issuedBookRepository.save(issuedBook);
            System.out.println("Issued book saved successfully for " + issuedBook.getEmail());

             //Decrease quantity
            manageBook.setQuantity(manageBook.getQuantity() - 1);
            manageBookRepository.save(manageBook);
            return "Book issued with the ISBN.";

        } else {
            return "Book with given ISBN not found.";
        }
    }

        public List<IssuedBook> findAllIssuedBook () {
            return issuedBookRepository.findAll();
        }


    //  Calculate Fine
    public long calculateFine(IssuedBook issuedBook) {
        if (issuedBook.getReturnDate() != null) {
            LocalDate today = LocalDate.now();
            if (issuedBook.getReturnDate().isBefore(today)) {
                long daysLate = ChronoUnit.DAYS.between(issuedBook.getReturnDate(), today);
                return daysLate * 10; // Fine is 10 per day
            }
        }
        return 0;
    }
    public List<IssuedBook> findIssuedBooksByEmail(String email) {
        return issuedBookRepository.findByEmail(email);
    }



}