package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.Defaulter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaulterRepository extends JpaRepository<Defaulter, Long> {

    long count();

    void deleteByEmail(String email);

    List<Defaulter> findByEmail(String email);

    List<Defaulter> findByEmailContainingIgnoreCase(String email);
}
