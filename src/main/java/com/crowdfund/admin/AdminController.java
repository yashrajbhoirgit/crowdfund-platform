package com.crowdfund.admin;

import com.crowdfund.admin.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<PlatformStatsDto> getStats() {
        return ResponseEntity.ok(adminService.getPlatformStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("id", id));
    }

    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable Long id) {
        adminService.suspendUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        adminService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/campaigns")
    public ResponseEntity<List<Map<String, Object>>> getCampaigns() {
        return ResponseEntity.ok(adminService.getAllCampaigns());
    }

    @PutMapping("/campaigns/{id}/approve")
    public ResponseEntity<Void> approveCampaign(@PathVariable Long id) {
        adminService.approveCampaign(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/campaigns/{id}/reject")
    public ResponseEntity<Void> rejectCampaign(@PathVariable Long id) {
        adminService.rejectCampaign(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/campaigns/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        adminService.deleteCampaign(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/donations")
    public ResponseEntity<List<Map<String, Object>>> getDonations() {
        return ResponseEntity.ok(adminService.getAllDonations());
    }

    @GetMapping("/analytics/monthly")
    public ResponseEntity<List<MonthlyDataDto>> getMonthlyData() {
        return ResponseEntity.ok(adminService.getMonthlyDonationData());
    }

    @GetMapping("/analytics/categories")
    public ResponseEntity<List<CategoryDataDto>> getCategoryData() {
        return ResponseEntity.ok(adminService.getCategoryWiseData());
    }

    @GetMapping("/analytics/top-donors")
    public ResponseEntity<List<TopDonorDto>> getTopDonors() {
        return ResponseEntity.ok(adminService.getTopDonors());
    }

    @GetMapping("/analytics/top-campaigns")
    public ResponseEntity<List<Map<String, Object>>> getTopCampaigns() {
        List<Map<String, Object>> topCampaigns = new ArrayList<>();
        topCampaigns.add(Map.of("title", "Save the Forest", "raised", 25000));
        return ResponseEntity.ok(topCampaigns);
    }

    @GetMapping(value = "/reports/users/csv", produces = "text/csv")
    public ResponseEntity<byte[]> downloadUsersCsv() {
        byte[] csv = adminService.generateUsersCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @GetMapping(value = "/reports/donations/csv", produces = "text/csv")
    public ResponseEntity<byte[]> downloadDonationsCsv() {
        byte[] csv = adminService.generateDonationsCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=donations.csv");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        return ResponseEntity.ok().headers(headers).body(csv);
    }
}
