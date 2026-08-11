package com.crowdfund.admin.dto;

import java.math.BigDecimal;

public class CategoryDataDto {
    private String category;
    private BigDecimal totalAmount;
    private long campaignCount;

    public CategoryDataDto(String category, BigDecimal totalAmount, long campaignCount) {
        this.category = category;
        this.totalAmount = totalAmount;
        this.campaignCount = campaignCount;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public long getCampaignCount() { return campaignCount; }
    public void setCampaignCount(long campaignCount) { this.campaignCount = campaignCount; }
}
