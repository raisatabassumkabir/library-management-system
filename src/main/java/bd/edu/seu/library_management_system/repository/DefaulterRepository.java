package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.Defaulter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefaulterRepository extends JpaRepository<Defaulter, String> {

    long count();
}
