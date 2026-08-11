package com.crowdfund.admin.dto;

import java.math.BigDecimal;

public class MonthlyDataDto {
    private String month;
    private BigDecimal totalAmount;
    private int donationCount;

    public MonthlyDataDto(String month, BigDecimal totalAmount, int donationCount) {
        this.month = month;
        this.totalAmount = totalAmount;
        this.donationCount = donationCount;
    }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public int getDonationCount() { return donationCount; }
    public void setDonationCount(int donationCount) { this.donationCount = donationCount; }
}
