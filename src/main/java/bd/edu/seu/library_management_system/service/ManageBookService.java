package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.repository.ManageBookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManageBookService {
    private final ManageBookRepository manageBookRepository;

    public ManageBookService(ManageBookRepository manageBookRepository) {
        this.manageBookRepository = manageBookRepository;
    }
    public void manageBook(ManageBook manageBook) {
        manageBookRepository.save(manageBook);
    }

    public ManageBook getBookByIsbn(int isbn) {
        return manageBookRepository.findByIsbn(isbn).orElse(null);
    }


    public List<ManageBook>getAllBooks() {
        return manageBookRepository.findAll();
    }

    public ManageBook updateBook(int isbn, ManageBook manageUpdateBook) {
        Optional<ManageBook>optionalManageBook=manageBookRepository.findById(manageUpdateBook.getIsbn());
        if(optionalManageBook.isPresent()) {
           ManageBook optionalBookUpdate=optionalManageBook.get();
           optionalBookUpdate.setTitle(manageUpdateBook.getTitle());
           optionalBookUpdate.setAuthor(manageUpdateBook.getAuthor());
           optionalBookUpdate.setQuantity(manageUpdateBook.getQuantity());
           return manageBookRepository.save(optionalBookUpdate);


        }
        return null;
    }

    public void deleteBook(int isbn) {

        manageBookRepository.deleteById(isbn);
    }

}
