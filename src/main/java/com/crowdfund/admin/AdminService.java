package com.crowdfund.admin;

import com.crowdfund.admin.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AdminService {

    @Autowired
    private AnalyticsService analyticsService;

    public PlatformStatsDto getPlatformStats() {
        PlatformStatsDto stats = new PlatformStatsDto();
        stats.setTotalUsers(1250);
        stats.setTotalCampaigns(340);
        stats.setActiveCampaigns(120);
        stats.setTotalDonations(5600);
        stats.setTotalRaisedAmount(new BigDecimal("1540000.00"));
        stats.setNewUsersThisMonth(45);
        stats.setPendingApprovals(12);
        return stats;
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
        String csv = "ID,Name,Email,Status\n1,Admin User,admin@test.com,ACTIVE\n";
        return csv.getBytes();
    }

    public byte[] generateDonationsCsv() {
        String csv = "ID,Amount,Campaign,Date\n1,100,Save the Forest,2023-01-01\n";
        return csv.getBytes();
    }

    public void suspendUser(Long id) {
        // Logic to suspend user
    }

    public void activateUser(Long id) {
        // Logic to activate user
    }

    public void deleteUser(Long id) {
        // Logic to delete user
    }

    public void approveCampaign(Long id) {
        // Logic to approve campaign
    }

    public void rejectCampaign(Long id) {
        // Logic to reject campaign
    }

    public void deleteCampaign(Long id) {
        // Logic to delete campaign
    }
}
