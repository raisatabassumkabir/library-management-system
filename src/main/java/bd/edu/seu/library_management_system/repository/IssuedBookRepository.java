package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.IssuedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {

    Optional<IssuedBook> findByIsbnAndEmail(int isbn, String email);

    List<IssuedBook> findByEmailIgnoreCase(String email);

    long countByIsbn(int isbn);
}
