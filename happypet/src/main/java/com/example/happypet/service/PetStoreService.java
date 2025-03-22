package com.example.happypet.service;

import com.example.happypet.dto.PetStoreDTO;
import com.example.happypet.model.PetStore;
import com.example.happypet.repo.PetStoreRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetStoreService {

    @Autowired
    private PetStoreRepo petStoreRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<PetStoreDTO> getAllPets() {
        List<PetStore> pets = petStoreRepo.findAll();
        return pets.stream()
                .map(pet -> {
                    PetStoreDTO dto = modelMapper.map(pet, PetStoreDTO.class);
                    if (pet.getPhoto() != null) {
                        dto.setPhoto(Base64.getEncoder().encodeToString(pet.getPhoto()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public PetStoreDTO savePet(PetStoreDTO petStoreDTO, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty() && !photo.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
        }

        PetStore pet = modelMapper.map(petStoreDTO, PetStore.class);
        pet.setPhoto(photo != null ? photo.getBytes() : null);
        petStoreRepo.save(pet);
        return modelMapper.map(pet, PetStoreDTO.class);
    }

    public PetStoreDTO updatePet(int id, PetStoreDTO petStoreDTO, MultipartFile photo) throws IOException {
        Optional<PetStore> existingPetOptional = petStoreRepo.findById(id);

        if (existingPetOptional.isPresent()) {
            PetStore existingPet = existingPetOptional.get();

            // Update fields from DTO
            existingPet.setName(petStoreDTO.getName());
            existingPet.setType(petStoreDTO.getType());
            existingPet.setBreed(petStoreDTO.getBreed());
            existingPet.setGender(petStoreDTO.getGender());
            existingPet.setAge(petStoreDTO.getAge());
            existingPet.setPrice(petStoreDTO.getPrice());
            existingPet.setStatus(petStoreDTO.getStatus());

            // Update the photo if a new one is provided
            if (photo != null && !photo.isEmpty()) {
                if (!photo.getContentType().startsWith("image")) {
                    throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
                }
                existingPet.setPhoto(photo.getBytes());
            }

            petStoreRepo.save(existingPet);
            return modelMapper.map(existingPet, PetStoreDTO.class);
        } else {
            throw new RuntimeException("Pet with ID " + id + " not found.");
        }
    }


    public String deletePet(Integer petId){
        petStoreRepo.deleteById(petId);
        return "Pet successfully deleted";
    }

    public PetStoreDTO updatePetStatus(int id, PetStoreDTO petStoreDTO) {
        Optional<PetStore> existingPetOptional = petStoreRepo.findById(id);
        if (existingPetOptional.isPresent()) {
            PetStore existingPet = existingPetOptional.get();
            // Update ONLY the status field
            existingPet.setStatus(petStoreDTO.getStatus());

            return modelMapper.map(petStoreRepo.save(existingPet), PetStoreDTO.class);
        } else {
            throw new RuntimeException("Pet with ID " + id + " not found.");
        }
    }
}
