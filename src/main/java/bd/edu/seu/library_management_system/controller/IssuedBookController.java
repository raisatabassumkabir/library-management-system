package bd.edu.seu.library_management_system.controller;
import bd.edu.seu.library_management_system.model.IssuedBook;
import bd.edu.seu.library_management_system.service.IssuedBookService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@SpringBootApplication
public class IssuedBookController {

    private final IssuedBookService issuedBookService;

    public IssuedBookController(IssuedBookService issuedBookService) {
        this.issuedBookService = issuedBookService;

    }
    @GetMapping("/issuedBook")
    public String issuedBookPage(Model model) {
        model.addAttribute("issuedBook", new IssuedBook());
        model.addAttribute("issuedBookList", issuedBookService.findAllIssuedBook());
        return "issuedBook";
    }

    @PostMapping("/admin/issued-book")
    public String showIssuedBook(@ModelAttribute IssuedBook issuedBook, Model model) {
        String message= issuedBookService.saveIssuedBook(issuedBook);
        model.addAttribute("issuedBook", new IssuedBook());
        model.addAttribute("issuedBookList", issuedBookService.findAllIssuedBook());
        model.addAttribute("message",message);

        return "issuedBook";
    }


}
