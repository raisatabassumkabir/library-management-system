package bd.edu.seu.library_management_system.controller;

import bd.edu.seu.library_management_system.model.Review;
import bd.edu.seu.library_management_system.model.ManageBook;
import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
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

    public BookRatingController(ManageBookService manageBookService, ReviewService reviewService,
            RegistrationRepository registrationRepository) {
        this.manageBookService = manageBookService;
        this.reviewService = reviewService;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping("/book/{isbn}")
    public String bookDetails(@PathVariable int isbn, Model model, Principal principal) {
        ManageBook book = manageBookService.getBookByIsbn(isbn);
        List<Review> reviews = reviewService.getReviewsByBook(isbn);

        // Calculate average rating
        double avgRating = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("reviewCount", reviews.size());

        if (principal != null) {
            String email = principal.getName();
            model.addAttribute("userEmail", email);

            // Check if user already reviewed? maybe later
            Optional<Registration> user = registrationRepository.findById(email);
            model.addAttribute("userName", user.map(Registration::getName).orElse("User"));
        }

        model.addAttribute("newReview", new Review());

        return "bookRatingDetails";
    }

    @PostMapping("/book/{isbn}/review")
    public String addReview(@PathVariable int isbn, @ModelAttribute Review review, Principal principal) {
        if (principal == null) {
            return "redirect:/login?error";
        }

        String email = principal.getName();
        Optional<Registration> user = registrationRepository.findById(email);

        review.setBookIsbn(isbn);
        review.setUserEmail(email);
        review.setUserName(user.map(Registration::getName).orElse("Anonymous"));

        reviewService.addReview(review);
        return "redirect:/book/" + isbn;
    }
}
