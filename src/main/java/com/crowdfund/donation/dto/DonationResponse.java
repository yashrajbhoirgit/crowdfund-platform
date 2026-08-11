package com.crowdfund.donation.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DonationResponse {
    private Long id;
    private BigDecimal amount;
    private String message;
    private boolean isAnonymous;
    private String paymentStatus;
    private String transactionId;
    private String razorpayOrderId;
    private Long donorId;
    private String donorName;
    private Long campaignId;
    private String campaignTitle;
    private String donatedAt;
}
