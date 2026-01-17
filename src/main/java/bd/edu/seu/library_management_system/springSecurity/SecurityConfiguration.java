package bd.edu.seu.library_management_system.springSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
public class SecurityConfiguration {

        private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
        private final CustomAuthenticationProvider customAuthenticationProvider;

        public SecurityConfiguration(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                        CustomAuthenticationProvider customAuthenticationProvider) {
                this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
                this.customAuthenticationProvider = customAuthenticationProvider;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.authorizeHttpRequests(request -> request
                                // Static resources
                                .requestMatchers(new AntPathRequestMatcher("/picture/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/uploads/**")).permitAll()
                                // Public Pages (Must come BEFORE /admin/** or other broad restrictions)
                                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin")).permitAll() // Admin Login Page
                                .requestMatchers(new AntPathRequestMatcher("/registration")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/index")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/home")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/gallery")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/registration-form")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/student-login-form")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/teacher-login-form")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin-login-form")).permitAll()
                                // Secured Admin Endpoints
                                .requestMatchers(new AntPathRequestMatcher("/adminDashboard"))
                                .hasAuthority("ROLE_ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/adminDashboard/**"))
                                .hasAuthority("ROLE_ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
                                // Secured Student/Teacher Endpoints
                                .requestMatchers(new AntPathRequestMatcher("/studentDashboard"))
                                .hasAuthority("ROLE_STUDENT")
                                .requestMatchers(new AntPathRequestMatcher("/studentDashboard/**"))
                                .hasAuthority("ROLE_STUDENT")
                                .requestMatchers(new AntPathRequestMatcher("/teacherDashboard"))
                                .hasAuthority("ROLE_TEACHER")
                                .requestMatchers(new AntPathRequestMatcher("/teacherDashboard/**"))
                                .hasAuthority("ROLE_TEACHER")
                                .anyRequest()
                                .authenticated())
                                .formLogin(form -> form.loginPage("/login") // Keep default or custom login if mapped
                                                .loginProcessingUrl("/login") // This is where forms should POST
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .successHandler(customAuthenticationSuccessHandler) // We'll handle
                                                                                                    // routing here
                                                .failureUrl("/index?error=true") // Redirect back to index/login on
                                                                                 // failure
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/user-info-form", "/saveContactDetails",
                                                                "/registration-form", "/login")) // Ignore CSRF for
                                                                                                 // login/registration
                                                                                                 // if needed, though
                                                                                                 // better to use tokens
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/index")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .permitAll())
                                .build();

        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                // Configure AuthenticationManagerBuilder explicitly with our provider
                return http.getSharedObject(
                                org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder.class)
                                .authenticationProvider(customAuthenticationProvider)
                                .build();
        }

}
