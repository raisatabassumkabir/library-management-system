package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.ReturnBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnBookRepository extends JpaRepository<ReturnBook, Long> {

}
