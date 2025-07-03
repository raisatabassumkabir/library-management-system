package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.ManageBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface ManageBookRepository extends JpaRepository<ManageBook,Integer> {

    Optional<ManageBook> findByIsbn(int isbn);
    @Query("SELECT SUM(m.quantity) FROM ManageBook m")
    long sumQuantities();

}
