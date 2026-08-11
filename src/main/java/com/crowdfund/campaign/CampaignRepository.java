package com.crowdfund.campaign;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(CampaignStatus status);
    List<Campaign> findByOwnerId(Long ownerId);
    List<Campaign> findByCategoryAndStatus(String category, CampaignStatus status);
    List<Campaign> searchByTitleContainingIgnoreCaseAndStatus(String keyword, CampaignStatus status);
    List<Campaign> findTop6ByStatusOrderByRaisedAmountDesc(CampaignStatus status);
    Page<Campaign> findByStatusOrderByCreatedAtDesc(CampaignStatus status, Pageable pageable);
}
