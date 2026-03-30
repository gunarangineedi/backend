package com.loka.userservice.config;

import com.loka.userservice.model.User;
import com.loka.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("demo@loka.com")) {
            User demo = User.builder()
                .name("Demo User")
                .email("demo@loka.com")
                .password(passwordEncoder.encode("password123"))
                .role("USER")
                .build();
            userRepository.save(demo);
            log.info("✓ Demo user created: demo@loka.com / password123");
        }

        if (!userRepository.existsByEmail("admin@loka.com")) {
            User admin = User.builder()
                .name("LOKA Admin")
                .email("admin@loka.com")
                .password(passwordEncoder.encode("admin123"))
                .role("ADMIN")
                .build();
            userRepository.save(admin);
            log.info("✓ Admin user created: admin@loka.com / admin123");
        }
    }
}
