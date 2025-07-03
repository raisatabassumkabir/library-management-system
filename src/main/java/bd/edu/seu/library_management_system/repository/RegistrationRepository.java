package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,String> {


    Optional<Registration> findByEmailAndPassword(String email, String password);
    Optional<Registration> findByEmail(String email);
}
