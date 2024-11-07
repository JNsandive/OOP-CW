package com.movie.ticketing.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (for JWT-based authentication)
                .csrf(CsrfConfigurer::disable)

                // Authorization configuration
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/auth/**","/start-ticketing-system").permitAll() // Allow public access to auth endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )

                // Make the session stateless for JWT-based authentication
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define BCrypt as the password encoder
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Provide AuthenticationManager bean
    }

    // WebSecurityCustomizer to allow static resources to be accessed without security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
