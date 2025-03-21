package com.example.happypet.controller;
import com.example.happypet.dto.PetDTO;
import com.example.happypet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        PetDTO pet = petService.getPetById(id);
        if (pet != null) {
            return ResponseEntity.ok(pet);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PetDTO> addPet(
            @RequestParam("name") String name,
            @RequestParam("petType") String petType,
            @RequestParam("price") Double price,
            @RequestParam("breed") String breed,
            @RequestParam("birthYear") String birthYear,
            @RequestParam("gender") String gender,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        PetDTO petDTO = new PetDTO();
        petDTO.setName(name);
        petDTO.setPetType(petType);
        petDTO.setPrice(price);
        petDTO.setBreed(breed);
        petDTO.setBirthYear(birthYear);
        petDTO.setGender(gender);
        petDTO.setImage(image);

        PetDTO savedPet = petService.savePet(petDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("petType") String petType,
            @RequestParam("price") Double price,
            @RequestParam("breed") String breed,
            @RequestParam("birthYear") String birthYear,
            @RequestParam("gender") String gender,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        PetDTO petDTO = new PetDTO();
        petDTO.setName(name);
        petDTO.setPetType(petType);
        petDTO.setPrice(price);
        petDTO.setBreed(breed);
        petDTO.setBirthYear(birthYear);
        petDTO.setGender(gender);
        petDTO.setImage(image);

        PetDTO updatedPet = petService.updatePet(id, petDTO);
        if (updatedPet != null) {
            return ResponseEntity.ok(updatedPet);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        boolean deleted = petService.deletePet(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<PetDTO> buyPet(@PathVariable Long id) {
        PetDTO purchasedPet = petService.purchasePet(id);
        if (purchasedPet != null) {
            return ResponseEntity.ok(purchasedPet);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/type/{petType}")
    public ResponseEntity<List<PetDTO>> getPetsByType(@PathVariable String petType) {
        return ResponseEntity.ok(petService.getPetsByType(petType));
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<PetDTO>> getPetsByGender(@PathVariable String gender) {
        return ResponseEntity.ok(petService.getPetsByGender(gender));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetDTO>> searchPets(@RequestParam String query) {
        return ResponseEntity.ok(petService.searchPets(query));
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] imageData = petService.getImage(fileName);
            HttpHeaders headers = new HttpHeaders();

            // Set content type based on file extension
            if (fileName.toLowerCase().endsWith(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (fileName.toLowerCase().endsWith(".gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
