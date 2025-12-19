package bd.edu.seu.library_management_system.service;


import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    private  final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public void saveRegistration(Registration registration) {
        registrationRepository.save(registration);
        

    }
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
