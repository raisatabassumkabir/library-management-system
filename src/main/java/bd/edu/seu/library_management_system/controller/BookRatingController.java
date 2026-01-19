package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.model.Review;
import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.IssuedBookRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.repository.ReturnBookRepository;
import bd.edu.seu.library_management_system.service.ManageBookService;
import bd.edu.seu.library_management_system.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookRatingController {

    private final ManageBookService manageBookService;
    private final ReviewService reviewService;
    private final RegistrationRepository registrationRepository;

    private final IssuedBookRepository issuedBookRepository;
    private final ReturnBookRepository returnBookRepository;

    public BookRatingController(ManageBookService manageBookService, ReviewService reviewService,
            RegistrationRepository registrationRepository, IssuedBookRepository issuedBookRepository,
            ReturnBookRepository returnBookRepository) {
        this.manageBookService = manageBookService;
        this.reviewService = reviewService;
        this.registrationRepository = registrationRepository;
        this.issuedBookRepository = issuedBookRepository;
        this.returnBookRepository = returnBookRepository;
    }

    @GetMapping("/book/{isbn}")
    public String bookDetails(@PathVariable int isbn, Model model, Principal principal) {
        ManageBook book = manageBookService.getBookByIsbn(isbn);
        List<Review> reviews = reviewService.getReviewsByBook(isbn);

        // Calculate average rating (ignore 0 ratings if you want purely star-based avg,
        // or keep them if 0 means bad)
        // Usually 0 means "no rating given".
        double avgRating = reviews.stream()
                .filter(r -> r.getRating() > 0)
                .mapToInt(Review::getRating).average().orElse(0.0);

        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("avgRatingVal", avgRating);
        model.addAttribute("reviewCount", reviews.size());

        boolean canReview = false;
        if (principal != null) {
            String email = principal.getName();
            model.addAttribute("userEmail", email);

            Optional<Registration> user = registrationRepository.findById(email);
            model.addAttribute("userName", user.map(Registration::getName).orElse("User"));

            // Check if user has borrowed currently OR returned in the past
            boolean isIssued = issuedBookRepository.existsByIsbnAndEmail(isbn, email);
            boolean isReturned = returnBookRepository.existsByIsbnAndEmail(isbn, email);

            canReview = isIssued || isReturned;
        }

        model.addAttribute("canReview", canReview);
        model.addAttribute("newReview", new Review());

        return "bookRatingDetails";
    }

    @PostMapping("/book/{isbn}/review")
    public String addReview(@PathVariable int isbn, @ModelAttribute Review review, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();

        // Security check: Must vary permission again
        boolean isIssued = issuedBookRepository.existsByIsbnAndEmail(isbn, email);
        boolean isReturned = returnBookRepository.existsByIsbnAndEmail(isbn, email);

        if (!isIssued && !isReturned) {
            return "redirect:/book/" + isbn + "?error=not_authorized";
        }

        // Validation: At least Rating OR Comment
        if (review.getRating() == 0 && (review.getComment() == null || review.getComment().trim().isEmpty())) {
            return "redirect:/book/" + isbn + "?error=empty_review";
        }

        Optional<Registration> user = registrationRepository.findById(email);

        review.setBookIsbn(isbn);
        review.setUserEmail(email);
        review.setUserName(user.map(Registration::getName).orElse("Anonymous"));

        reviewService.addReview(review);
        return "redirect:/book/" + isbn;
    }
}
