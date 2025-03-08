package com.example.happyPet.controller;


import com.example.happyPet.model.ContactRequest;
import com.example.happyPet.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // Allows requests from frontend
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ContactRequest contactRequest) {
        try {
            contactService.sendEmail(contactRequest);
            return ResponseEntity.ok("Message sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message.");
        }
    }
}
