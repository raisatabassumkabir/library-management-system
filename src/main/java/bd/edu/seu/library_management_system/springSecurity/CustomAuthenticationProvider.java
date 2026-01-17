package bd.edu.seu.library_management_system.springSecurity;

import bd.edu.seu.library_management_system.model.Admin;
import bd.edu.seu.library_management_system.model.Registration;
import bd.edu.seu.library_management_system.repository.AdminRepository;
import bd.edu.seu.library_management_system.repository.RegistrationRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final RegistrationRepository registrationRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(RegistrationRepository registrationRepository,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.registrationRepository = registrationRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("DEBUG: Authenticating User: " + email);

        // 1. Try to find user in Registration table (Student/Teacher)
        Optional<Registration> registrationOptional = registrationRepository.findByEmail(email);

        if (registrationOptional.isPresent()) {
            Registration registration = registrationOptional.get();
            System.out.println("DEBUG: User found in Registration table. Type: " + registration.getUserType());

            boolean passwordMatches = false;
            boolean isPlainText = false;

            // 1. Try BCrypt
            if (passwordEncoder.matches(password, registration.getPassword())) {
                passwordMatches = true;
            }
            // 2. Fallback to Plain Text
            else if (password.equals(registration.getPassword())) {
                System.out.println("DEBUG: Plain text password matched for user: " + email);
                passwordMatches = true;
                isPlainText = true;
            }

            if (passwordMatches) {
                // Auto-Migrate to BCrypt if plain text was used
                if (isPlainText) {
                    System.out.println("DEBUG: Migrating password to BCrypt for user: " + email);
                    registration.setPassword(passwordEncoder.encode(password));
                    registrationRepository.save(registration);
                }

                String role = "ROLE_USER"; // Default
                if ("student".equalsIgnoreCase(registration.getUserType())) {
                    role = "ROLE_STUDENT";
                } else if ("teacher".equalsIgnoreCase(registration.getUserType())) {
                    role = "ROLE_TEACHER";
                }

                System.out.println("DEBUG: Authentication successful. Assigned Role: " + role);

                return new UsernamePasswordAuthenticationToken(
                        email,
                        password,
                        Collections.singletonList(new SimpleGrantedAuthority(role)));
            } else {
                System.out.println("DEBUG: Password did NOT match for user: " + email);
            }
        } else {
            System.out.println("DEBUG: User not found in Registration table: " + email);
        }

        // 2. Try to find user in Admin table
        Optional<Admin> adminOptional = adminRepository.findById(email);

        if (adminOptional.isPresent()) {
            System.out.println("DEBUG: User found in Admin table.");
            Admin admin = adminOptional.get();

            boolean passwordMatches = false;
            boolean isPlainText = false;

            // 1. Try BCrypt
            if (passwordEncoder.matches(password, admin.getPassword())) {
                passwordMatches = true;
            }
            // 2. Fallback to Plain Text
            else if (password.equals(admin.getPassword())) {
                System.out.println("DEBUG: Plain text password matched for ADMIN: " + email);
                passwordMatches = true;
                isPlainText = true;
            }

            if (passwordMatches) {
                // Auto-Migrate Admin to BCrypt
                if (isPlainText) {
                    System.out.println("DEBUG: Migrating ADMIN password to BCrypt: " + email);
                    admin.setPassword(passwordEncoder.encode(password));
                    adminRepository.save(admin);
                }

                System.out.println("DEBUG: Admin authentication successful.");
                return new UsernamePasswordAuthenticationToken(
                        email,
                        password,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            } else {
                System.out.println("DEBUG: Admin password did NOT match.");
            }
        } else {
            System.out.println("DEBUG: User not found in Admin table either: " + email);
        }

        System.out.println("DEBUG: Authentication failed for " + email);
        throw new BadCredentialsException("Invalid email or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
