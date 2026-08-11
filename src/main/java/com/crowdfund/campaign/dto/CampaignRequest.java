package com.crowdfund.campaign.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CampaignRequest {
    private String title;
    private String description;
    private String shortDescription;
    private String category;
    private BigDecimal goalAmount;
    private LocalDate deadline;
}
