package com.crowdfund.campaign;

import com.crowdfund.campaign.dto.CampaignRequest;
import com.crowdfund.campaign.dto.CampaignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Transactional
    public CampaignResponse createCampaign(CampaignRequest req, Long ownerId, String ownerName) {
        Campaign campaign = Campaign.builder()
            .title(req.getTitle())
            .description(req.getDescription())
            .shortDescription(req.getShortDescription())
            .category(req.getCategory())
            .goalAmount(req.getGoalAmount())
            .deadline(req.getDeadline())
            .ownerId(ownerId)
            .ownerName(ownerName)
            .status(CampaignStatus.ACTIVE)
            .build();
        return CampaignResponse.fromEntity(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponse updateCampaign(Long id, CampaignRequest req, Long ownerId) {
        Campaign campaign = campaignRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
            
        if (!campaign.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Not authorized to update this campaign");
        }
        
        campaign.setTitle(req.getTitle());
        campaign.setDescription(req.getDescription());
        campaign.setShortDescription(req.getShortDescription());
        campaign.setCategory(req.getCategory());
        campaign.setGoalAmount(req.getGoalAmount());
        campaign.setDeadline(req.getDeadline());
        
        return CampaignResponse.fromEntity(campaignRepository.save(campaign));
    }

    @Transactional
    public void deleteCampaign(Long id, Long ownerId) {
        Campaign campaign = campaignRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
            
        if (ownerId != -1 && !campaign.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Not authorized to delete this campaign");
        }
        
        campaignRepository.delete(campaign);
    }

    public CampaignResponse getCampaignById(Long id) {
        return campaignRepository.findById(id)
            .map(CampaignResponse::fromEntity)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    public Page<CampaignResponse> getAllActiveCampaigns(int page, int size) {
        return campaignRepository.findByStatusOrderByCreatedAtDesc(CampaignStatus.ACTIVE, PageRequest.of(page, size))
            .map(CampaignResponse::fromEntity);
    }

    public List<CampaignResponse> getCampaignsByCategory(String category) {
        return campaignRepository.findByCategoryAndStatus(category, CampaignStatus.ACTIVE).stream()
            .map(CampaignResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public List<CampaignResponse> searchCampaigns(String keyword) {
        return campaignRepository.searchByTitleContainingIgnoreCaseAndStatus(keyword, CampaignStatus.ACTIVE).stream()
            .map(CampaignResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public List<CampaignResponse> getMyCampaigns(Long ownerId) {
        return campaignRepository.findByOwnerId(ownerId).stream()
            .map(CampaignResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<CampaignResponse> getFeaturedCampaigns() {
        return campaignRepository.findTop6ByStatusOrderByRaisedAmountDesc(CampaignStatus.ACTIVE).stream()
            .map(CampaignResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional
    public CampaignResponse updateCampaignStatus(Long id, CampaignStatus status) {
        Campaign campaign = campaignRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
        campaign.setStatus(status);
        return CampaignResponse.fromEntity(campaignRepository.save(campaign));
    }

    @Transactional
    public void updateRaisedAmount(Long campaignId, BigDecimal amount) {
        Campaign campaign = campaignRepository.findById(campaignId)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
        campaign.setRaisedAmount(campaign.getRaisedAmount().add(amount));
        campaign.setDonorsCount(campaign.getDonorsCount() + 1);
        if (campaign.getRaisedAmount().compareTo(campaign.getGoalAmount()) >= 0) {
            campaign.setStatus(CampaignStatus.FUNDED);
        }
        campaignRepository.save(campaign);
    }
    
    @Transactional
    public void updateImageUrl(Long campaignId, String imageUrl) {
        Campaign campaign = campaignRepository.findById(campaignId)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
        campaign.setImageUrl(imageUrl);
        campaignRepository.save(campaign);
    }
}
