package com.example.happypet.controller;

import com.example.happypet.dto.UserWithInquiriesDTO;
import com.example.happypet.model.PetInquiry;
import com.example.happypet.service.DTOMapperService;
import com.example.happypet.service.PetInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin
@RequestMapping("api/v1/inquiries")
public class PetInquiryController {

    private static final Logger logger = LoggerFactory.getLogger(PetInquiryController.class);

    @Autowired
    private PetInquiryService petInquiryService;

    @Autowired
    private DTOMapperService dtoMapperService;

    @GetMapping
    public ResponseEntity<List<PetInquiry>> getAllInquiries() {
        List<PetInquiry> inquiries = petInquiryService.getAllInquiries();
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetInquiry> getInquiryById(@PathVariable Long id) {
        PetInquiry inquiry = petInquiryService.getInquiryById(id);
        return ResponseEntity.ok(inquiry);
    }

    @PostMapping
    public ResponseEntity<PetInquiry> createInquiry(@RequestBody PetInquiry inquiry) {
        PetInquiry createdInquiry = petInquiryService.createInquiry(inquiry);
        return new ResponseEntity<>(createdInquiry, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetInquiry> updateInquiry(@PathVariable Long id, @RequestBody PetInquiry inquiry) {
        PetInquiry updatedInquiry = petInquiryService.updateInquiry(id, inquiry);
        return ResponseEntity.ok(updatedInquiry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        petInquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PetInquiry> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        PetInquiry.InquiryStatus status = PetInquiry.InquiryStatus.valueOf(statusUpdate.get("status"));
        PetInquiry updatedInquiry = petInquiryService.updateInquiryStatus(id, status);
        return ResponseEntity.ok(updatedInquiry);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<PetInquiry>> getInquiriesByEmail(@PathVariable String email) {
        List<PetInquiry> inquiries = petInquiryService.getInquiriesByEmail(email);
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<PetInquiry>> getInquiriesByPetId(@PathVariable Long petId) {
        List<PetInquiry> inquiries = petInquiryService.getInquiriesByPetId(petId);
        return ResponseEntity.ok(inquiries);
    }

    // Original dashboard endpoint (kept for backward compatibility)
    @GetMapping("/admin/dashboard/raw")
    public ResponseEntity<List<PetInquiry>> getDashboardRawData() {
        List<PetInquiry> dashboardData = petInquiryService.getDashboardData();
        return ResponseEntity.ok(dashboardData);
    }

    // New dashboard endpoint that returns formatted data for the frontend
    @GetMapping("/admin/dashboard")
    public ResponseEntity<List<UserWithInquiriesDTO>> getDashboardData() {
        logger.info("Received request for admin dashboard data");

        try {
            List<UserWithInquiriesDTO> dashboardData = petInquiryService.getDashboardDataFormatted();

            // Log information about the result
            if (dashboardData == null) {
                logger.error("getDashboardDataFormatted returned null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            logger.info("Returning dashboard data with {} users", dashboardData.size());

            if (!dashboardData.isEmpty()) {
                UserWithInquiriesDTO firstUser = dashboardData.get(0);
                logger.info("First user: userId={}, name={}, email={}, pets count={}",
                        firstUser.getUserId(),
                        firstUser.getName(),
                        firstUser.getEmail(),
                        firstUser.getInterestedPets() != null ? firstUser.getInterestedPets().size() : "null");
            }

            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            logger.error("Error retrieving dashboard data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PetInquiry> createInquiryForm(@RequestBody PetInquiry inquiry) {
        PetInquiry createdInquiry = petInquiryService.createInquiry(inquiry);
        return new ResponseEntity<>(createdInquiry, HttpStatus.CREATED);
    }
}
