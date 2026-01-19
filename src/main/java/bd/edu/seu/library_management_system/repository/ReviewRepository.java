package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.Review;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByBookIsbn(int bookIsbn);
}
