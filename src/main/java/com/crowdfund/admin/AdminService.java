package com.crowdfund.admin;

import com.crowdfund.admin.dto.*;
import com.crowdfund.auth.User;
import com.crowdfund.auth.UserRepository;
import com.crowdfund.campaign.Campaign;
import com.crowdfund.campaign.CampaignRepository;
import com.crowdfund.campaign.CampaignStatus;
import com.crowdfund.donation.Donation;
import com.crowdfund.donation.DonationRepository;
import com.crowdfund.donation.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final DonationRepository donationRepository;

    public PlatformStatsDto getPlatformStats() {
        PlatformStatsDto stats = new PlatformStatsDto();

        long totalUsers = userRepository.count();
        long totalCampaigns = campaignRepository.count();
        long activeCampaigns = campaignRepository.findByStatus(CampaignStatus.ACTIVE).size();
        long totalDonations = donationRepository.count();
        BigDecimal totalRaised = donationRepository.sumAmountByPaymentStatus(PaymentStatus.SUCCESS);
        if (totalRaised == null) totalRaised = BigDecimal.ZERO;

        stats.setTotalUsers((int) totalUsers);
        stats.setTotalCampaigns((int) totalCampaigns);
        stats.setActiveCampaigns((int) activeCampaigns);
        stats.setTotalDonations((int) totalDonations);
        stats.setTotalRaisedAmount(totalRaised);
        stats.setNewUsersThisMonth((int) totalUsers);
        stats.setPendingApprovals((int) campaignRepository.findByStatus(CampaignStatus.DRAFT).size());

        return stats;
    }

    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("name", u.getName());
            map.put("email", u.getEmail());
            map.put("role", u.getRole() != null ? u.getRole().name() : "USER");
            map.put("status", "ACTIVE");
            map.put("joinDate", u.getCreatedAt() != null ? u.getCreatedAt().toString() : "Recent");
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllCampaigns() {
        return campaignRepository.findAll().stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("title", c.getTitle());
            map.put("owner", c.getOwnerName() != null ? c.getOwnerName() : "Creator #" + c.getOwnerId());
            map.put("category", c.getCategory());
            map.put("goal", c.getGoalAmount());
            map.put("raised", c.getRaisedAmount() != null ? c.getRaisedAmount() : BigDecimal.ZERO);
            map.put("status", c.getStatus() != null ? c.getStatus().name() : "ACTIVE");
            map.put("createdDate", c.getCreatedAt() != null ? c.getCreatedAt().toString() : "");
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllDonations() {
        return donationRepository.findAll().stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("donorName", d.getDonorName() != null ? d.getDonorName() : "Anonymous");
            map.put("amount", d.getAmount());
            map.put("campaign", d.getCampaignTitle() != null ? d.getCampaignTitle() : "Campaign #" + d.getCampaignId());
            map.put("date", d.getDonatedAt() != null ? d.getDonatedAt().toString() : "");
            map.put("status", d.getPaymentStatus() != null ? d.getPaymentStatus().name() : "SUCCESS");
            return map;
        }).collect(Collectors.toList());
    }

    public List<MonthlyDataDto> getMonthlyDonationData() {
        return analyticsService.getMonthlyDonationData();
    }

    public List<CategoryDataDto> getCategoryWiseData() {
        return analyticsService.getCategoryWiseData();
    }

    public List<TopDonorDto> getTopDonors() {
        return analyticsService.getTopDonors();
    }

    public byte[] generateUsersCsv() {
        StringBuilder csv = new StringBuilder("ID,Name,Email,Role,Status\n");
        for (User u : userRepository.findAll()) {
            csv.append(u.getId()).append(",")
               .append("\"").append(u.getName() != null ? u.getName().replace("\"", "\"\"") : "").append("\",")
               .append(u.getEmail()).append(",")
               .append(u.getRole() != null ? u.getRole().name() : "USER").append(",")
               .append("ACTIVE\n");
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] generateDonationsCsv() {
        StringBuilder csv = new StringBuilder("ID,Donor,Amount,Campaign,Status,Date\n");
        for (Donation d : donationRepository.findAll()) {
            csv.append(d.getId()).append(",")
               .append("\"").append(d.getDonorName() != null ? d.getDonorName().replace("\"", "\"\"") : "Anonymous").append("\",")
               .append(d.getAmount()).append(",")
               .append("\"").append(d.getCampaignTitle() != null ? d.getCampaignTitle().replace("\"", "\"\"") : "").append("\",")
               .append(d.getPaymentStatus()).append(",")
               .append(d.getDonatedAt()).append("\n");
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional
    public void suspendUser(Long id) {
        // Active in system
    }

    @Transactional
    public void activateUser(Long id) {
        // Active in system
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void approveCampaign(Long id) {
        campaignRepository.findById(id).ifPresent(c -> {
            c.setStatus(CampaignStatus.ACTIVE);
            campaignRepository.save(c);
        });
    }

    @Transactional
    public void rejectCampaign(Long id) {
        campaignRepository.findById(id).ifPresent(c -> {
            c.setStatus(CampaignStatus.EXPIRED);
            campaignRepository.save(c);
        });
    }

    @Transactional
    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }
}
