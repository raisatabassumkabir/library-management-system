package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface AdminRepository extends JpaRepository<Admin, String> {


    Optional<Admin>findAdminByEmailAndPassword(String email, String password);
}
