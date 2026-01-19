package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Review;
import bd.edu.seu.library_management_system.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void addReview(Review review) {
        review.setReviewDate(LocalDate.now());
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByBook(int isbn) {
        return reviewRepository.findByBookIsbn(isbn);
    }
}
