package com.crowdfund.donation;

import com.crowdfund.donation.dto.DonationRequest;
import com.crowdfund.donation.dto.PaymentVerificationRequest;
import com.crowdfund.donation.dto.DonationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Value("${razorpay.key.id:dummy_key}")
    private String razorpayKeyId;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateDonation(@RequestBody DonationRequest req) {
        Long currentUserId = 1L; 
        String currentUserName = "John Doe";
        String campaignTitle = "Sample Campaign Title"; 

        try {
            DonationResponse response = donationService.initiateDonation(req, currentUserId, currentUserName, campaignTitle);
            
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", response.getRazorpayOrderId());
            result.put("amount", response.getAmount());
            result.put("currency", "INR");
            result.put("key", razorpayKeyId);
            result.put("donationId", response.getId());
            
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
        Long currentUserId = 1L; 
        return ResponseEntity.ok(donationService.getMyDonations(currentUserId));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<DonationResponse>> getCampaignDonations(@PathVariable Long campaignId) {
        return ResponseEntity.ok(donationService.getCampaignDonations(campaignId));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long id) {
        Long currentUserId = 1L; 
        try {
            byte[] pdfBytes = donationService.generateAndDownloadReceipt(id, currentUserId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "receipt_" + id + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
