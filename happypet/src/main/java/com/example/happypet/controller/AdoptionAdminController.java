package com.example.happypet.controller;

import com.example.happypet.model.AdoptionPet;
import com.example.happypet.model.AdoptionPetGender;
import com.example.happypet.model.AdoptionStatus;
import com.example.happypet.repo.AdoptionPetRepository;
import com.example.happypet.service.AdoptionImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class AdoptionAdminController {

    @Autowired
    private AdoptionPetRepository adoptionPetRepository;

    @Autowired
    private AdoptionImageStorageService imageStorageService;

    // Get all pets
    @GetMapping("/gets")
    public List<AdoptionPet> getAllPets() {
        List<AdoptionPet> pets = adoptionPetRepository.findAll();
        System.out.println("All pets: " + pets); // Debugging
        return pets;
    }

    // Add a new pet
    @PostMapping("/addpet")
    public ResponseEntity<String> addPet(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("age") String age,
            @RequestParam("gender") String gender,
            @RequestParam("breed") String breed,
            @RequestParam("adoptionStatus") String adoptionStatus,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("photo") MultipartFile photo) throws IOException {

        AdoptionPet pet = new AdoptionPet();
        pet.setPet_name(name);
        pet.setPet_species(type);

        // Handle the age conversion safely
        try {
            int petAge = Integer.parseInt(age);
            pet.setPet_age(petAge);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid age provided. Age must be a number.");
        }

        try {
            pet.setPet_gender(AdoptionPetGender.valueOf(gender));
            pet.setStatus(AdoptionStatus.valueOf(adoptionStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid gender or adoption status");
        }

        pet.setPet_breed(breed);
        pet.setPet_description(description != null ? description : "");

        // Store the image and get back the URL path
        String imageUrl = imageStorageService.storeImage(photo);
        pet.setImagePath(imageUrl);

        System.out.println("Image URL: " + imageUrl);  // Debugging line
        System.out.println("Pet object BEFORE saving: " + pet);

        // Set creation timestamp
        pet.setCreated_at(LocalDateTime.now());

        adoptionPetRepository.save(pet);
        return ResponseEntity.ok("Pet added successfully!");
    }

    // Add a test upload
    @PostMapping("/uploadTest")
    public ResponseEntity<String> uploadTest(@RequestParam("photo") MultipartFile photo) {
        try {
            String imageUrl = imageStorageService.storeImage(photo);
            return ResponseEntity.ok("Image uploaded successfully. URL: " + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    // Update pet details
    @PutMapping("/updatepet")
    public ResponseEntity<String> updatePet(@RequestParam("id") int id,
                                            @RequestParam("name") String name,
                                            @RequestParam("type") String type,
                                            @RequestParam("age") String age,
                                            @RequestParam("gender") String gender,
                                            @RequestParam("breed") String breed,
                                            @RequestParam("adoptionStatus") String adoptionStatus,
                                            @RequestParam(value = "description", required = false) String description,
                                            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        Optional<AdoptionPet> optionalPet = adoptionPetRepository.findById(id);
        if (optionalPet.isPresent()) {
            AdoptionPet pet = optionalPet.get();

            pet.setPet_name(name);
            pet.setPet_species(type);

            int petAge;
            try {
                if (age == null || age.isEmpty() || "NaN".equals(age)) {
                    petAge = pet.getPet_age();
                } else {
                    petAge = Integer.parseInt(age);
                }
            } catch (NumberFormatException e) {
                petAge = pet.getPet_age();
            }
            pet.setPet_age(petAge);

            try {
                pet.setPet_gender(AdoptionPetGender.valueOf(gender));
                pet.setStatus(AdoptionStatus.valueOf(adoptionStatus));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid gender or adoption status");
            }

            pet.setPet_breed(breed);
            pet.setPet_description(description != null ? description : pet.getPet_description());

            if (photo != null && !photo.isEmpty()) {
                String imageUrl = imageStorageService.storeImage(photo);
                pet.setImagePath(imageUrl);
                System.out.println("Updated Image URL: " + imageUrl);
            }

            adoptionPetRepository.save(pet);
            return ResponseEntity.ok("Pet updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a pet
    @DeleteMapping("/deletepet/{id}")
    public ResponseEntity<String> deletePet(@PathVariable int id) {
        if (adoptionPetRepository.existsById(id)) {
            adoptionPetRepository.deleteById(id);
            return ResponseEntity.ok("Pet deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet not found");
        }
    }
}
