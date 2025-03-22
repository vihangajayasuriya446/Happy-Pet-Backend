package com.example.happypet.controller;

import com.example.happypet.dto.PetStoreDTO;
import com.example.happypet.service.PetStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class PetStoreController {

    @Autowired
    private PetStoreService petStoreService;

    @GetMapping("/getallstores")
    public List<PetStoreDTO> getAllPets() {
        return petStoreService.getAllPets();
    }

    @PostMapping("/addstore")
    public PetStoreDTO savePet(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("breed") String breed,
            @RequestParam("gender") String gender,
            @RequestParam("age") String age,
            @RequestParam("price") double price,
            @RequestParam("status") String status,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) throws IOException {

        return petStoreService.savePet(createPetStoreDTO(name, type, breed, gender, age, price, status), photo);
    }

    @PutMapping("/updatestore/{id}")
    public ResponseEntity<PetStoreDTO> updatePet(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("breed") String breed,
            @RequestParam("gender") String gender,
            @RequestParam("age") String age,
            @RequestParam("price") double price,
            @RequestParam("status") String status,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) throws IOException {

        PetStoreDTO updatedPetDTO = petStoreService.updatePet(
                id,
                createPetStoreDTO(name, type, breed, gender, age, price, status),
                photo);
        return new ResponseEntity<>(updatedPetDTO, HttpStatus.OK);
    }

    @DeleteMapping("/deletestore/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Integer id) {
        petStoreService.deletePet(id);
        return new ResponseEntity<>("Pet deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/updatestorestatus/{id}")
    public ResponseEntity<PetStoreDTO> updatePetStatus(@PathVariable int id, @RequestBody PetStoreDTO petStoreDTO) {
        PetStoreDTO updatedPet = petStoreService.updatePetStatus(id, petStoreDTO);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    // Helper method to create a PetStoreDTO
    private PetStoreDTO createPetStoreDTO(String name, String type, String breed, String gender,
                                          String age, double price, String status) {
        PetStoreDTO petStoreDTO = new PetStoreDTO();
        petStoreDTO.setName(name);
        petStoreDTO.setType(type);
        petStoreDTO.setBreed(breed);
        petStoreDTO.setGender(gender);
        petStoreDTO.setAge(age);
        petStoreDTO.setPrice(price);
        petStoreDTO.setStatus(status);
        return petStoreDTO;
    }
}
