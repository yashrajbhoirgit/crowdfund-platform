package com.crowdfund.donation;

import com.crowdfund.donation.dto.DonationRequest;
import com.crowdfund.donation.dto.PaymentVerificationRequest;
import com.crowdfund.donation.dto.DonationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfReceiptService pdfReceiptService;

    @Transactional
    public DonationResponse initiateDonation(DonationRequest req, Long donorId, String donorName, String campaignTitle) {
        String receipt = "txn_" + UUID.randomUUID().toString().substring(0, 8);
        String orderId = razorpayService.createOrder(req.getAmount(), "INR", receipt);

        Donation donation = Donation.builder()
                .amount(req.getAmount())
                .message(req.getMessage())
                .isAnonymous(req.isAnonymous())
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(receipt)
                .razorpayOrderId(orderId)
                .donorId(donorId)
                .donorName(donorName)
                .campaignId(req.getCampaignId())
                .campaignTitle(campaignTitle)
                .build();

        donation = donationRepository.save(donation);
        return mapToResponse(donation);
    }

    @Transactional
    public DonationResponse verifyAndCompleteDonation(PaymentVerificationRequest req) {
        Donation donation = donationRepository.findById(req.getDonationId())
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        boolean isValid = razorpayService.verifyPaymentSignature(
                req.getRazorpayOrderId(), req.getRazorpayPaymentId(), req.getRazorpaySignature()
        );

        if (isValid) {
            donation.setPaymentStatus(PaymentStatus.SUCCESS);
            donation.setRazorpayPaymentId(req.getRazorpayPaymentId());
            donation.setRazorpaySignature(req.getRazorpaySignature());
            donation = donationRepository.save(donation);

            emailService.sendDonationConfirmation(
                    "donor@example.com", 
                    donation.getDonorName(),
                    donation.getAmount(),
                    donation.getCampaignTitle(),
                    donation.getTransactionId()
            );

            return mapToResponse(donation);
        } else {
            donation.setPaymentStatus(PaymentStatus.FAILED);
            donationRepository.save(donation);
            throw new RuntimeException("Payment verification failed");
        }
    }

    public List<DonationResponse> getMyDonations(Long donorId) {
        return donationRepository.findByDonorId(donorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DonationResponse> getCampaignDonations(Long campaignId) {
        return donationRepository.findByCampaignIdAndPaymentStatus(campaignId, PaymentStatus.SUCCESS).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public byte[] generateAndDownloadReceipt(Long donationId, Long userId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        if (!donation.getDonorId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        return pdfReceiptService.generateReceipt(donation);
    }

    private DonationResponse mapToResponse(Donation donation) {
        DonationResponse res = new DonationResponse();
        res.setId(donation.getId());
        res.setAmount(donation.getAmount());
        res.setMessage(donation.getMessage());
        res.setAnonymous(donation.isAnonymous());
        res.setPaymentStatus(donation.getPaymentStatus().name());
        res.setTransactionId(donation.getTransactionId());
        res.setRazorpayOrderId(donation.getRazorpayOrderId());
        res.setDonorId(donation.getDonorId());
        res.setDonorName(donation.getDonorName());
        res.setCampaignId(donation.getCampaignId());
        res.setCampaignTitle(donation.getCampaignTitle());
        res.setDonatedAt(donation.getDonatedAt() != null ? donation.getDonatedAt().toString() : "");
        return res;
    }
}
