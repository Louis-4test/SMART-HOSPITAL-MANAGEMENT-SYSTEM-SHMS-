package com.hms.config;

import com.hms.entity.User;
import com.hms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Users already exist, skipping data initialization");
            return;
        }

        log.info("Initializing default users...");
        userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "ADMIN"));
        userRepository.save(new User("doctor", passwordEncoder.encode("doctor123"), "DOCTOR"));
        userRepository.save(new User("nurse", passwordEncoder.encode("nurse123"), "NURSE"));
        userRepository.save(new User("receptionist", passwordEncoder.encode("reception123"), "RECEPTIONIST"));
        log.info("Default users created successfully");
    }
}
