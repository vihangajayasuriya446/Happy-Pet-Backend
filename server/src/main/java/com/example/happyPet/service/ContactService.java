package com.example.happyPet.service;

import com.example.happyPet.model.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(ContactRequest contactRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("arshana.20222147@iit.ac.lk");
        message.setSubject("New Contact Form Submission from Happy Pet");
        message.setText("From: " + contactRequest.getEmail() + "\n\nMessage:\n" + contactRequest.getMessage());

        mailSender.send(message);
    }
}
