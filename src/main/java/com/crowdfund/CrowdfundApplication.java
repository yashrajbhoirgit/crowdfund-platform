package com.crowdfund;

import com.crowdfund.auth.Role;
import com.crowdfund.auth.User;
import com.crowdfund.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CrowdfundApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByEmail("admin@test.com").ifPresentOrElse(
                admin -> {
                    if (admin.getRole() != Role.ADMIN) {
                        admin.setRole(Role.ADMIN);
                        userRepository.save(admin);
                    }
                },
                () -> {
                    User admin = User.builder()
                            .name("Admin User")
                            .email("admin@test.com")
                            .password(passwordEncoder.encode("password123"))
                            .role(Role.ADMIN)
                            .bio("Platform Administrator")
                            .build();
                    userRepository.save(admin);
                }
            );
        };
    }
}
