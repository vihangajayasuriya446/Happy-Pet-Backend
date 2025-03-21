package com.example.happypet.service;

import com.example.happypet.dto.PetDTO;
import com.example.happypet.repo.PetRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepo petRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileStorageService fileStorageService;

    public List<PetDTO> getAllPets() {
        List<com.example.happypet.model.Pet> pets = petRepo.findAll();
        return modelMapper.map(pets, new TypeToken<List<PetDTO>>() {}.getType());
    }

    public PetDTO getPetById(Long id) {
        Optional<com.example.happypet.model.Pet> petOptional = petRepo.findById(id);
        if (petOptional.isPresent()) {
            return modelMapper.map(petOptional.get(), PetDTO.class);
        }
        return null;
    }

    public PetDTO savePet(PetDTO petDTO) {
        try {
            // Handle image upload if present
            if (petDTO.getImage() != null && !petDTO.getImage().isEmpty()) {
                String fileName = fileStorageService.saveImage(petDTO.getImage());
                petDTO.setImageUrl("/api/v1/pets/images/" + fileName);
            }

            com.example.happypet.model.Pet pet = modelMapper.map(petDTO, com.example.happypet.model.Pet.class);
            petRepo.save(pet);
            return modelMapper.map(pet, PetDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save pet: " + e.getMessage());
        }
    }

    public PetDTO updatePet(Long id, PetDTO petDTO) {
        Optional<com.example.happypet.model.Pet> existingPetOptional = petRepo.findById(id);
        if (existingPetOptional.isPresent()) {
            com.example.happypet.model.Pet existingPet = existingPetOptional.get();

            // Update fields
            existingPet.setName(petDTO.getName());
            existingPet.setPetType(petDTO.getPetType());
            existingPet.setPrice(petDTO.getPrice());
            existingPet.setBreed(petDTO.getBreed());
            existingPet.setBirthYear(petDTO.getBirthYear());
            existingPet.setGender(petDTO.getGender());

            // Handle image update if present
            if (petDTO.getImage() != null && !petDTO.getImage().isEmpty()) {
                try {
                    // Delete old image if exists
                    if (existingPet.getImageUrl() != null && !existingPet.getImageUrl().isEmpty()) {
                        String oldFileName = extractFileNameFromUrl(existingPet.getImageUrl());
                        fileStorageService.deleteImage(oldFileName);
                    }

                    // Save new image
                    String fileName = fileStorageService.saveImage(petDTO.getImage());
                    existingPet.setImageUrl("/api/v1/pets/images/" + fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to update pet image: " + e.getMessage());
                }
            }

            petRepo.save(existingPet);
            return modelMapper.map(existingPet, PetDTO.class);
        }
        return null;
    }

    public boolean deletePet(Long id) {
        Optional<com.example.happypet.model.Pet> petOptional = petRepo.findById(id);
        if (petOptional.isPresent()) {
            com.example.happypet.model.Pet pet = petOptional.get();

            // Delete image file if exists
            if (pet.getImageUrl() != null && !pet.getImageUrl().isEmpty()) {
                try {
                    String fileName = extractFileNameFromUrl(pet.getImageUrl());
                    fileStorageService.deleteImage(fileName);
                } catch (IOException e) {
                    // Log error but continue with deletion
                    e.printStackTrace();
                }
            }

            petRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public PetDTO purchasePet(Long id) {
        Optional<com.example.happypet.model.Pet> petOptional = petRepo.findById(id);
        if (petOptional.isPresent()) {
            com.example.happypet.model.Pet pet = petOptional.get();
            pet.setPurchased(true);
            petRepo.save(pet);
            return modelMapper.map(pet, PetDTO.class);
        }
        return null;
    }

    public List<PetDTO> getPetsByType(String petType) {
        List<com.example.happypet.model.Pet> pets = petRepo.findByPetType(petType);
        return modelMapper.map(pets, new TypeToken<List<PetDTO>>() {}.getType());
    }

    // Added method to get pets by gender
    public List<PetDTO> getPetsByGender(String gender) {
        List<com.example.happypet.model.Pet> pets = petRepo.findByGenderIgnoreCase(gender);
        return modelMapper.map(pets, new TypeToken<List<PetDTO>>() {}.getType());
    }


    public List<PetDTO> getPetsByBreed(String breed) {
        List<com.example.happypet.model.Pet> pets = petRepo.findByBreedContainingIgnoreCase(breed);
        return modelMapper.map(pets, new TypeToken<List<PetDTO>>() {}.getType());
    }

    public List<PetDTO> searchPets(String query) {
        List<com.example.happypet.model.Pet> pets = petRepo.findByNameContainingIgnoreCase(query);
        return modelMapper.map(pets, new TypeToken<List<PetDTO>>() {}.getType());
    }

    // Helper method to extract filename from URL
    private String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl != null && imageUrl.contains("/")) {
            return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        }
        return imageUrl;
    }

    public byte[] getImage(String fileName) throws IOException {
        return fileStorageService.getImage(fileName);
    }
}
