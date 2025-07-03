package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.model.Student;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import bd.edu.seu.library_management_system.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    public StudentService(StudentRepository studentRepository, RegistrationRepository registrationRepository) {
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    public void saveStudentLogin(Student student) {
        studentRepository.save(student);
    }

    public boolean studentLoginAuthentication(String email, String password) {
        Optional<Registration> registrationOptional = registrationRepository.findByEmail(email);

        if (registrationOptional.isPresent()) {
            Registration registration = registrationOptional.get();
            if (registration.getEmail().equalsIgnoreCase(email.trim())
                    && registration.getPassword().equals(password)
                    && registration.getUserType().equalsIgnoreCase("student")) {
                return true;
            }
        }
        return false;
    }
}
