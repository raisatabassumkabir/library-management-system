package bd.edu.seu.library_management_system.controller;
import bd.edu.seu.library_management_system.model.ReturnBook;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import bd.edu.seu.library_management_system.service.ReturnBookService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@SpringBootApplication
public class ReturnBookController {

    private final IssuedBookService issuedBookService;
    private final ReturnBookService returnBookService;


    public ReturnBookController(IssuedBookService issuedBookService, ReturnBookService returnBookService) {
        this.issuedBookService = issuedBookService;
        this.returnBookService = returnBookService;
    }
    @GetMapping("/returnBook")
    public String ReturnBookPage(Model model) {
        model.addAttribute("returnBookList", returnBookService.getAllReturnBook());
        return "returnBook";
    }

    @PostMapping("/admin/return-book")
        public String returnBookPage(@RequestParam int isbn,@RequestParam String email,@ModelAttribute ReturnBook returnBook, Model model) {
        LocalDate returnDate = LocalDate.now(); //// capture today's date
        returnBookService.saveReturnBook(returnBook);
        returnBookService.returnIssuedBook(isbn, email, returnDate);
        model.addAttribute("returnBook",new ReturnBook());
        model.addAttribute("returnBookList",returnBookService.getAllReturnBook());

        return "redirect:/returnBook";
    }
}
