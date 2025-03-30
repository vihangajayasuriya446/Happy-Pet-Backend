package com.example.happypet.service;

import com.example.happypet.dto.ContactFormDTO;
import com.example.happypet.model.ContactForm;
import com.example.happypet.repo.ContactFormRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactFormService {

    @Autowired
    private ContactFormRepository contactFormRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Save a contact form submission
    // In ContactFormService.java
    public ContactFormDTO saveContactForm(ContactFormDTO contactFormDTO) {
        ContactForm contactForm = modelMapper.map(contactFormDTO, ContactForm.class);
        ContactForm savedForm = contactFormRepository.save(contactForm);
        return modelMapper.map(savedForm, ContactFormDTO.class); // This will include the id
    }

    public List<ContactFormDTO> getAllContactForms() {
        List<ContactForm> contactForms = contactFormRepository.findAll();
        return contactForms.stream()
                .map(form -> modelMapper.map(form, ContactFormDTO.class)) // This will include the id
                .collect(Collectors.toList());
    }

    // Delete a contact form submission by ID
    public void deleteContactForm(Long id) {
        contactFormRepository.deleteById(id);
    }

    public ContactFormDTO updateContactFormStatus(Long id, ContactFormDTO contactFormDTO) {
        Optional<ContactForm> existingFormOptional = contactFormRepository.findById(id);
        if (existingFormOptional.isPresent()) {
            ContactForm existingForm = existingFormOptional.get();
            existingForm.setStatus(contactFormDTO.getStatus());
            return modelMapper.map(contactFormRepository.save(existingForm), ContactFormDTO.class);
        } else {
            throw new RuntimeException("Contact form with ID " + id + " not found.");
        }
    }

}