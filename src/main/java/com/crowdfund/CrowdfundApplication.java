package com.crowdfund;

import com.crowdfund.auth.Role;
import com.crowdfund.auth.User;
import com.crowdfund.auth.UserRepository;
import com.crowdfund.campaign.Campaign;
import com.crowdfund.campaign.CampaignRepository;
import com.crowdfund.campaign.CampaignStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class CrowdfundApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDefaultData(UserRepository userRepository, 
                                             CampaignRepository campaignRepository, 
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Admin Users
            String[] adminEmails = {"admin@crowdfund.com", "admin@test.com"};
            User defaultAdmin = null;
            for (String email : adminEmails) {
                User admin = userRepository.findByEmail(email).orElseGet(() -> {
                    User u = User.builder()
                            .name("Platform Admin")
                            .email(email)
                            .password(passwordEncoder.encode("password123"))
                            .role(Role.ADMIN)
                            .bio("Platform Administrator & Moderator")
                            .build();
                    return userRepository.save(u);
                });
                if (defaultAdmin == null) defaultAdmin = admin;
            }

            // Seed Sample Campaigns if DB is empty
            if (campaignRepository.count() == 0 && defaultAdmin != null) {
                campaignRepository.save(Campaign.builder()
                        .title("Tree Plantation Drive 2026")
                        .category("Environment")
                        .goalAmount(new BigDecimal("50000"))
                        .raisedAmount(new BigDecimal("18500"))
                        .shortDescription("Planting 5,000 indigenous trees to restore biodiversity.")
                        .description("Join our green mission to plant indigenous shade and fruit-bearing trees across degraded rural lands. Every contribution funds saplings, fencing, and organic drip irrigation.")
                        .imageUrl("https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800&q=80")
                        .deadline(LocalDate.now().plusDays(45))
                        .status(CampaignStatus.ACTIVE)
                        .ownerId(defaultAdmin.getId())
                        .ownerName(defaultAdmin.getName())
                        .donorsCount(24)
                        .build());

                campaignRepository.save(Campaign.builder()
                        .title("Clean Water for Rural Schools")
                        .category("Health")
                        .goalAmount(new BigDecimal("75000"))
                        .raisedAmount(new BigDecimal("32000"))
                        .shortDescription("Installing solar-powered RO water filtration units.")
                        .description("Providing safe, tested drinking water to over 1,200 school children across underprivileged rural districts to eliminate waterborne illnesses.")
                        .imageUrl("https://images.unsplash.com/photo-1574482620826-40685ca5ebd2?w=800&q=80")
                        .deadline(LocalDate.now().plusDays(30))
                        .status(CampaignStatus.ACTIVE)
                        .ownerId(defaultAdmin.getId())
                        .ownerName(defaultAdmin.getName())
                        .donorsCount(41)
                        .build());

                campaignRepository.save(Campaign.builder()
                        .title("Digital Literacy for Underprivileged Girls")
                        .category("Education")
                        .goalAmount(new BigDecimal("100000"))
                        .raisedAmount(new BigDecimal("64500"))
                        .shortDescription("Equipping 10 community centers with computer labs.")
                        .description("Empowering young women through STEM education, computer proficiency, coding bootcamps, and career mentorship.")
                        .imageUrl("https://images.unsplash.com/photo-1509062522246-3755977927d7?w=800&q=80")
                        .deadline(LocalDate.now().plusDays(60))
                        .status(CampaignStatus.ACTIVE)
                        .ownerId(defaultAdmin.getId())
                        .ownerName(defaultAdmin.getName())
                        .donorsCount(58)
                        .build());
            }
        };
    }
}
