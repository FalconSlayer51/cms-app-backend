package org.ramesh.backend.service;

import org.ramesh.backend.domain.entities.User;
import org.ramesh.backend.domain.enums.Role;
import org.ramesh.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${bootstrap.admin.email}")
    private String adminEmail;

    @Value("${bootstrap.admin.password}")
    private String adminPassword;

    public AdminBootstrapRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (adminExists) {
            return; // already bootstrapped
        }

        User admin = new User(
                UUID.randomUUID(),
                adminEmail,
                passwordEncoder.encode(adminPassword),
                Role.ADMIN,
                OffsetDateTime.now()
        );

        userRepository.save(admin);
        System.out.println("Bootstrap ADMIN created: " + adminEmail);
    }
}
