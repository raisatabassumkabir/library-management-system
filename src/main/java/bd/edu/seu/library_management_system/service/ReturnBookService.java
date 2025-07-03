package bd.edu.seu.library_management_system.service;


import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.model.ReturnBook;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import bd.edu.seu.library_management_system.repository.ReturnBookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnBookService {

    private final ReturnBookRepository returnBookRepository;
    private final ManageBookRepository manageBookRepository;
    private final IssuedBookRepository issuedBookRepository;

    public ReturnBookService(ReturnBookRepository returnBookRepository, ManageBookRepository manageBookRepository, IssuedBookRepository issuedBookRepository) {
        this.returnBookRepository = returnBookRepository;
        this.manageBookRepository = manageBookRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    public void saveReturnBook(ReturnBook returnBook) {
        returnBookRepository.save(returnBook);
    }
    public List<ReturnBook>getAllReturnBook(){
        return returnBookRepository.findAll();
    }

    public String returnIssuedBook(int isbn, String email, LocalDate returnDate){

        Optional<IssuedBook>optionalIssuedBook=issuedBookRepository.findByIsbnAndEmail(isbn,email);
        if (optionalIssuedBook.isPresent()){
            IssuedBook issuedBook=optionalIssuedBook.get();
            issuedBook.setReturnDate(returnDate);
            issuedBookRepository.delete(issuedBook);
        }

        Optional<ManageBook>optionalManageBook=manageBookRepository.findByIsbn(isbn);
        if (optionalIssuedBook.isPresent()){
            ManageBook manageBook=optionalManageBook.get();
            manageBook.setQuantity(manageBook.getQuantity()+1);
            manageBookRepository.save(manageBook);
            return "Book returned successfully!";
         }   else {
            return "Issued book not found for the given ISBN and email.";
             }
    }


    }



