package com.crowdfund.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonorId(Long donorId);
    List<Donation> findByCampaignId(Long campaignId);
    List<Donation> findByCampaignIdAndPaymentStatus(Long campaignId, PaymentStatus status);

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaignId = :campaignId AND d.paymentStatus = :status")
    BigDecimal sumAmountByCampaignIdAndPaymentStatus(@Param("campaignId") Long campaignId, @Param("status") PaymentStatus status);

    long countByCampaignIdAndPaymentStatus(Long campaignId, PaymentStatus status);

    List<Donation> findTop5ByCampaignIdAndPaymentStatusOrderByDonatedAtDesc(Long campaignId, PaymentStatus status);

    @Query("SELECT COUNT(DISTINCT d.donorId) FROM Donation d WHERE d.paymentStatus = :status")
    long countDistinctDonorIdByPaymentStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.paymentStatus = :status")
    BigDecimal sumAmountByPaymentStatus(@Param("status") PaymentStatus status);

    List<Donation> findByDonatedAtBetweenAndPaymentStatus(LocalDateTime start, LocalDateTime end, PaymentStatus status);
}
