package com.example.happypet.controller;

import com.example.happypet.dto.ContactFormDTO;
import com.example.happypet.service.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactFormController {

    @Autowired
    private ContactFormService contactFormService;

    // Submit a contact form
    @PostMapping("/submit")
    public ResponseEntity<ContactFormDTO> submitContactForm(@RequestBody ContactFormDTO contactFormDTO) {
        ContactFormDTO savedForm = contactFormService.saveContactForm(contactFormDTO);
        return ResponseEntity.ok(savedForm);
    }

    // Get all contact form submissions
    @GetMapping("/responses")
    public ResponseEntity<List<ContactFormDTO>> getAllResponses() {
        List<ContactFormDTO> responses = contactFormService.getAllContactForms();
        return ResponseEntity.ok(responses);
    }

    // Delete a contact form submission by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long id) {
        contactFormService.deleteContactForm(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-status/{id}") // Using PUT for similar REST semantics
    public ResponseEntity<ContactFormDTO> updateContactFormStatus(
            @PathVariable Long id,
            @RequestBody ContactFormDTO contactFormDTO) {
        ContactFormDTO updatedContactForm = contactFormService.updateContactFormStatus(id, contactFormDTO);
        if (updatedContactForm != null) {
            return ResponseEntity.ok(updatedContactForm);
        }
        return ResponseEntity.notFound().build();
    }
}