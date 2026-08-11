package com.crowdfund.admin.dto;

import java.math.BigDecimal;

public class TopDonorDto {
    private String donorName;
    private String donorEmail;
    private BigDecimal totalDonated;
    private long donationCount;

    public TopDonorDto(String donorName, String donorEmail, BigDecimal totalDonated, long donationCount) {
        this.donorName = donorName;
        this.donorEmail = donorEmail;
        this.totalDonated = totalDonated;
        this.donationCount = donationCount;
    }

    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }

    public String getDonorEmail() { return donorEmail; }
    public void setDonorEmail(String donorEmail) { this.donorEmail = donorEmail; }

    public BigDecimal getTotalDonated() { return totalDonated; }
    public void setTotalDonated(BigDecimal totalDonated) { this.totalDonated = totalDonated; }

    public long getDonationCount() { return donationCount; }
    public void setDonationCount(long donationCount) { this.donationCount = donationCount; }
}
