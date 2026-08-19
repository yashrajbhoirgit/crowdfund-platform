package com.crowdfund.donation;

import com.crowdfund.auth.User;
import com.crowdfund.auth.UserRepository;
import com.crowdfund.campaign.Campaign;
import com.crowdfund.campaign.CampaignRepository;
import com.crowdfund.donation.dto.DonationRequest;
import com.crowdfund.donation.dto.DonationResponse;
import com.crowdfund.donation.dto.PaymentVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    @Value("${razorpay.key.id:dummy_key}")
    private String razorpayKeyId;

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ud) {
            return userRepository.findByEmail(ud.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("Not authenticated");
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateDonation(@RequestBody DonationRequest req) {
        User user = getAuthenticatedUser();
        String campaignTitle = "Crowdfunding Campaign";

        if (req.getCampaignId() != null) {
            Campaign c = campaignRepository.findById(req.getCampaignId()).orElse(null);
            if (c != null && c.getTitle() != null) {
                campaignTitle = c.getTitle();
            }
        }

        try {
            DonationResponse response = donationService.initiateDonation(req, user.getId(), user.getName(), campaignTitle);

            Map<String, Object> result = new HashMap<>();
            result.put("orderId", response.getRazorpayOrderId());
            result.put("amount", response.getAmount());
            result.put("currency", "INR");
            result.put("key", razorpayKeyId);
            result.put("donationId", response.getId());
            result.put("campaignTitle", campaignTitle);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyDonation(@RequestBody PaymentVerificationRequest req) {
        try {
            DonationResponse response = donationService.verifyAndCompleteDonation(req);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<DonationResponse>> getMyDonations() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(donationService.getMyDonations(user.getId()));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<DonationResponse>> getCampaignDonations(@PathVariable Long campaignId) {
        return ResponseEntity.ok(donationService.getCampaignDonations(campaignId));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        try {
            byte[] pdfBytes = donationService.generateAndDownloadReceipt(id, user.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "receipt_" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
