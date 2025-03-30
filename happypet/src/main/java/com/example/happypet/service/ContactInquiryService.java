package com.example.happypet.service;
import com.example.happypet.model.ContactInquiry;
import com.example.happypet.repo.ContactInquiryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactInquiryService {

    private static final Logger logger = LoggerFactory.getLogger(ContactInquiryService.class);

    @Autowired
    private ContactInquiryRepository contactInquiryRepository;

    public List<ContactInquiry> getAllContactInquiries() {
        logger.info("Fetching all contact inquiries");
        return contactInquiryRepository.findAll();
    }

    public ContactInquiry getContactInquiryById(Long id) {
        logger.info("Fetching contact inquiry with id: {}", id);
        return contactInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact inquiry not found with id: " + id));
    }

    public ContactInquiry createContactInquiry(ContactInquiry contactInquiry) {
        logger.info("Creating new contact inquiry from: {}", contactInquiry.getEmail());

        // Set default values if not provided
        if (contactInquiry.getStatus() == null) {
            contactInquiry.setStatus(ContactInquiry.InquiryStatus.NEW);
        }

        if (contactInquiry.getCreatedAt() == null) {
            contactInquiry.setCreatedAt(LocalDateTime.now());
        }

        return contactInquiryRepository.save(contactInquiry);
    }

    public ContactInquiry updateContactInquiry(Long id, ContactInquiry contactInquiry) {
        logger.info("Updating contact inquiry with id: {}", id);

        ContactInquiry existingInquiry = getContactInquiryById(id);

        existingInquiry.setName(contactInquiry.getName());
        existingInquiry.setEmail(contactInquiry.getEmail());
        existingInquiry.setPhoneNumber(contactInquiry.getPhoneNumber());
        existingInquiry.setAddress(contactInquiry.getAddress());
        existingInquiry.setMessage(contactInquiry.getMessage());

        if (contactInquiry.getStatus() != null) {
            existingInquiry.setStatus(contactInquiry.getStatus());
        }

        return contactInquiryRepository.save(existingInquiry);
    }

    public ContactInquiry updateContactInquiryStatus(Long id, ContactInquiry.InquiryStatus status) {
        logger.info("Updating status of contact inquiry with id: {} to {}", id, status);

        ContactInquiry existingInquiry = getContactInquiryById(id);
        existingInquiry.setStatus(status);

        return contactInquiryRepository.save(existingInquiry);
    }

    public void deleteContactInquiry(Long id) {
        logger.info("Deleting contact inquiry with id: {}", id);
        contactInquiryRepository.deleteById(id);
    }
}
