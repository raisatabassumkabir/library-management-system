package bd.edu.seu.library_management_system.repository;

import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

  Optional<Student> findByEmail(String email);
}
