package com.crowdfund.admin.dto;

import java.math.BigDecimal;

public class PlatformStatsDto {
    private long totalUsers;
    private long totalCampaigns;
    private long activeCampaigns;
    private long totalDonations;
    private BigDecimal totalRaisedAmount;
    private long newUsersThisMonth;
    private long pendingApprovals;

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalCampaigns() { return totalCampaigns; }
    public void setTotalCampaigns(long totalCampaigns) { this.totalCampaigns = totalCampaigns; }

    public long getActiveCampaigns() { return activeCampaigns; }
    public void setActiveCampaigns(long activeCampaigns) { this.activeCampaigns = activeCampaigns; }

    public long getTotalDonations() { return totalDonations; }
    public void setTotalDonations(long totalDonations) { this.totalDonations = totalDonations; }

    public BigDecimal getTotalRaisedAmount() { return totalRaisedAmount; }
    public void setTotalRaisedAmount(BigDecimal totalRaisedAmount) { this.totalRaisedAmount = totalRaisedAmount; }

    public long getNewUsersThisMonth() { return newUsersThisMonth; }
    public void setNewUsersThisMonth(long newUsersThisMonth) { this.newUsersThisMonth = newUsersThisMonth; }

    public long getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(long pendingApprovals) { this.pendingApprovals = pendingApprovals; }
}
