package com.crowdfund.donation.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DonationRequest {
    private Long campaignId;
    private BigDecimal amount;
    private String message;
    private boolean isAnonymous;
}
