package bd.edu.seu.library_management_system.springSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.authorizeHttpRequests(request -> request
                                .requestMatchers("/", "/registration", "/index", "/home", "/error", "/gallery",
                                                "/registration-form", "/student-login-form", "/teacher-login-form",
                                                "/admin-login-form",
                                                "/studentDashboard", "/teacherDashboard", "/adminDashboard",
                                                "/manageBook", "/defaulterList", "/viewMembers", "/issuedBook",
                                                "/returnBook", "/admin",
                                                "/admin/issued-book", "/admin/manage-book", "/admin/update-book",
                                                "/admin/delete-book/**",
                                                "/admin/clearDefaulter", "/admin/return-book",
                                                "/admin/books/search-fragment", "/admin/defaulters/search-fragment")
                                .permitAll()
                                .requestMatchers("/picture/**", "/css/**", "/uploads/**", "/user/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                                .formLogin(form -> form.loginPage("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .successForwardUrl("/user_dashboard")
                                                .failureForwardUrl("/login?error=true")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/user-info-form", "/saveContactDetails",
                                                                "/student-login-form", "/teacher-login-form",
                                                                "/admin-login-form", "/registration-form",
                                                                "/admin/**"))
                                .build();

        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
