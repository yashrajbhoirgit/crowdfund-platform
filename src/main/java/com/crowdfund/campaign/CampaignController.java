package com.crowdfund.campaign;

import com.crowdfund.campaign.dto.CampaignRequest;
import com.crowdfund.campaign.dto.CampaignResponse;
import com.crowdfund.auth.User;
import com.crowdfund.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;
    private final FileUploadService fileUploadService;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ud) {
            return userRepository.findByEmail(ud.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("Not authenticated");
    }

    @GetMapping
    public ResponseEntity<Page<CampaignResponse>> getAllActiveCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(campaignService.getAllActiveCampaigns(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CampaignResponse>> getCampaignsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(campaignService.getCampaignsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CampaignResponse>> searchCampaigns(@RequestParam String keyword) {
        return ResponseEntity.ok(campaignService.searchCampaigns(keyword));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<CampaignResponse>> getFeaturedCampaigns() {
        return ResponseEntity.ok(campaignService.getFeaturedCampaigns());
    }

    @GetMapping("/my")
    public ResponseEntity<List<CampaignResponse>> getMyCampaigns() {
        Long ownerId = getAuthenticatedUser().getId();
        return ResponseEntity.ok(campaignService.getMyCampaigns(ownerId));
    }

    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(@RequestBody CampaignRequest req) {
        User owner = getAuthenticatedUser();
        return ResponseEntity.ok(campaignService.createCampaign(req, owner.getId(), owner.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable Long id, @RequestBody CampaignRequest req) {
        Long ownerId = getAuthenticatedUser().getId();
        return ResponseEntity.ok(campaignService.updateCampaign(id, req, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        Long ownerId = (user.getRole() == com.crowdfund.auth.Role.ADMIN) ? -1L : user.getId();
        campaignService.deleteCampaign(id, ownerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.saveFile(file);
        campaignService.updateImageUrl(id, fileUrl);
        return ResponseEntity.ok(Map.of("imageUrl", fileUrl));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<CampaignResponse> updateStatus(@PathVariable Long id, @RequestParam CampaignStatus status) {
        return ResponseEntity.ok(campaignService.updateCampaignStatus(id, status));
    }
}
