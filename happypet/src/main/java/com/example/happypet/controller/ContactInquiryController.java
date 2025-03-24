package com.example.happypet.controller;
import com.example.happypet.model.ContactInquiry;
import com.example.happypet.service.ContactInquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/v1/inquiries/contact")
public class ContactInquiryController {

    private static final Logger logger = LoggerFactory.getLogger(ContactInquiryController.class);

    @Autowired
    private ContactInquiryService contactInquiryService;

    @GetMapping
    public ResponseEntity<List<ContactInquiry>> getAllContactInquiries() {
        logger.info("Received request to get all contact inquiries");
        List<ContactInquiry> inquiries = contactInquiryService.getAllContactInquiries();
        logger.info("Returning {} contact inquiries", inquiries.size());
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactInquiry> getContactInquiryById(@PathVariable Long id) {
        logger.info("Received request to get contact inquiry with id: {}", id);
        ContactInquiry inquiry = contactInquiryService.getContactInquiryById(id);
        return ResponseEntity.ok(inquiry);
    }

    @PostMapping
    public ResponseEntity<ContactInquiry> createContactInquiry(@RequestBody ContactInquiry contactInquiry) {
        logger.info("Received request to create new contact inquiry from: {}", contactInquiry.getEmail());
        ContactInquiry createdInquiry = contactInquiryService.createContactInquiry(contactInquiry);
        logger.info("Created contact inquiry with id: {}", createdInquiry.getId());
        return new ResponseEntity<>(createdInquiry, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInquiry> updateContactInquiry(
            @PathVariable Long id,
            @RequestBody ContactInquiry contactInquiry) {
        logger.info("Received request to update contact inquiry with id: {}", id);
        ContactInquiry updatedInquiry = contactInquiryService.updateContactInquiry(id, contactInquiry);
        return ResponseEntity.ok(updatedInquiry);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ContactInquiry> updateContactInquiryStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            logger.info("Received request to update status of contact inquiry with id: {} to {}",
                    id, statusUpdate.get("status"));

            ContactInquiry.InquiryStatus status = ContactInquiry.InquiryStatus.valueOf(statusUpdate.get("status"));
            ContactInquiry updatedInquiry = contactInquiryService.updateContactInquiryStatus(id, status);
            return ResponseEntity.ok(updatedInquiry);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value: {}", statusUpdate.get("status"), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInquiry(@PathVariable Long id) {
        logger.info("Received request to delete contact inquiry with id: {}", id);
        contactInquiryService.deleteContactInquiry(id);
        return ResponseEntity.noContent().build();
    }
}
