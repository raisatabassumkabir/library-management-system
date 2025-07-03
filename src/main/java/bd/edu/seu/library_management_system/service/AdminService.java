package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Admin;
import bd.edu.seu.library_management_system.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    public boolean adminAuthenticationByEmailAndPassword(String email, String password) {

        Optional<Admin> adminOptionalAuthentication = adminRepository.findAdminByEmailAndPassword(email, password);

        if (adminOptionalAuthentication.isPresent()) {
            Admin adminAuthentication = adminOptionalAuthentication.get();

            if (adminAuthentication.getEmail().equals(email) && adminAuthentication.getPassword().equals(password)) {
                return adminOptionalAuthentication.isPresent();

            }
        }


        return false;
    }



}
