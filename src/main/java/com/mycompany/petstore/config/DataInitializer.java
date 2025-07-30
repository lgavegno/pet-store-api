package com.mycompany.petstore.config;

import com.mycompany.petstore.model.Role;
import com.mycompany.petstore.model.User;
import com.mycompany.petstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test") // Don't run during tests
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("No users found, creating initial users...");
            
            // Create admin user
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@petstore.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            
            // Create regular user
            User user = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("user@petstore.com")
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build();
            
            userRepository.saveAll(List.of(admin, user));
            log.info("Created initial users: admin and user");
        }
    }
}
