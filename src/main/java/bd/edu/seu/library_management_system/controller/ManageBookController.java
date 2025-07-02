package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.service.ManageBookService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@SpringBootApplication
public class ManageBookController {

    private final ManageBookService manageBookService;

    public ManageBookController(ManageBookService manageBookService) {
        this.manageBookService = manageBookService;
    }


    @GetMapping("/manageBook")
    public String manageBookPage(Model model){
        model.addAttribute("manageBook", new ManageBook());
        model.addAttribute("manageBookList", manageBookService.getAllBooks());
        model.addAttribute("isUpdateMode", false);
        return "manageBook";
    }

    @PostMapping("/admin/manage-book")
    public String manageBookPage(@ModelAttribute ManageBook manageBook, Model model){
       manageBookService.manageBook(manageBook);
       model.addAttribute("manageBook", new ManageBook());
       model.addAttribute("manageBookList",manageBookService.getAllBooks());
       model.addAttribute("isUpdateMode",false);

        return "manageBook";
    }
    // Load book data into form
    @GetMapping("/admin/edit-book/{isbn}")
     public String editBook(@PathVariable int isbn, Model model) {
         ManageBook book = manageBookService.getBookByIsbn(isbn);
         model.addAttribute("manageBook",book); // pre-fill form with this book
         model.addAttribute("manageBookList", manageBookService.getAllBooks());// keep list visible
         model.addAttribute("isUpdateMode",true);
         return "manageBook";
     }


    // Process form submission and update book
    @PostMapping("/admin/update-book")
    public String updateBook(@ModelAttribute ManageBook manageBook, Model model) {
        manageBookService.updateBook(manageBook.getIsbn(), manageBook);
        model.addAttribute("manageBook", new ManageBook()); // reset form
        model.addAttribute("manageBookList", manageBookService.getAllBooks());
        model.addAttribute("isUpdateMode", false);
        return "manageBook";
    }


    @PostMapping("/admin/delete-book/{isbn}")
    public String deleteBook(@PathVariable int isbn, Model model) {
        manageBookService.deleteBook(isbn);
        model.addAttribute("manageBook", new ManageBook());
        model.addAttribute("manageBookList", manageBookService.getAllBooks());
        model.addAttribute("isUpdateMode",false);
       return "manageBook";
   }
}
