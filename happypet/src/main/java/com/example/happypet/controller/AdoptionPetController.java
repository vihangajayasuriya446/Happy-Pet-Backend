package com.example.happypet.controller;

import com.example.happypet.model.AdoptionPet;
import com.example.happypet.service.AdoptionPetService;
import com.example.happypet.service.AdoptionImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "http://localhost:5173")
public class AdoptionPetController {

    @Autowired
    private AdoptionPetService petService;

    @Autowired
    private AdoptionImageStorageService imageStorageService;

    @GetMapping("/available")
    public List<AdoptionPet> getAvailablePets() {
        List<AdoptionPet> pets = petService.getAvailablePets();
        System.out.println("Available Pets: " + pets);  // Debugging line
        return pets;
    }

    @GetMapping("/{id}")
    public AdoptionPet getPetById(@PathVariable int id) {
        AdoptionPet pet = petService.getPetById(id);
        System.out.println("Pet with ID " + id + ": " + pet);  // Debugging line
        return pet;
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadPetImage(
            @PathVariable int id,
            @RequestParam("image") MultipartFile image) {
        try {
            AdoptionPet pet = petService.getPetById(id);
            if (pet == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet not found");
            }

            String imagePath = imageStorageService.storeImage(image);
            if (imagePath == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store image.");
            }
            pet.setImagePath(imagePath);
            petService.savePet(pet);  // Assuming you have this method in PetService

            return ResponseEntity.ok(imagePath);
        } catch (Exception e) {
            System.err.println("Error uploading image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }
}
