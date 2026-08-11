package com.crowdfund.campaign.dto;

import com.crowdfund.campaign.Campaign;
import com.crowdfund.campaign.CampaignStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CampaignResponse {
    private Long id;
    private String title;
    private String description;
    private String shortDescription;
    private String category;
    private BigDecimal goalAmount;
    private BigDecimal raisedAmount;
    private LocalDate deadline;
    private CampaignStatus status;
    private String imageUrl;
    private String videoUrl;
    private Long ownerId;
    private String ownerName;
    private int donorsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int progressPercentage;

    public static CampaignResponse fromEntity(Campaign campaign) {
        CampaignResponse response = new CampaignResponse();
        response.setId(campaign.getId());
        response.setTitle(campaign.getTitle());
        response.setDescription(campaign.getDescription());
        response.setShortDescription(campaign.getShortDescription());
        response.setCategory(campaign.getCategory());
        response.setGoalAmount(campaign.getGoalAmount());
        response.setRaisedAmount(campaign.getRaisedAmount());
        response.setDeadline(campaign.getDeadline());
        response.setStatus(campaign.getStatus());
        response.setImageUrl(campaign.getImageUrl());
        response.setVideoUrl(campaign.getVideoUrl());
        response.setOwnerId(campaign.getOwnerId());
        response.setOwnerName(campaign.getOwnerName());
        response.setDonorsCount(campaign.getDonorsCount());
        response.setCreatedAt(campaign.getCreatedAt());
        response.setUpdatedAt(campaign.getUpdatedAt());
        
        if (campaign.getGoalAmount() != null && campaign.getGoalAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = campaign.getRaisedAmount()
                .multiply(new BigDecimal(100))
                .divide(campaign.getGoalAmount(), 0, RoundingMode.HALF_UP);
            response.setProgressPercentage(Math.min(progress.intValue(), 100));
        } else {
            response.setProgressPercentage(0);
        }
        
        return response;
    }
}
