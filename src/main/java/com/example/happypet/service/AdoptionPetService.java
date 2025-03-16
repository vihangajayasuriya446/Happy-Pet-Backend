package com.example.happypet.service;

import com.example.happypet.model.AdoptionPet;
import com.example.happypet.model.AdoptionStatus;
import com.example.happypet.repo.AdoptionPetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionPetService {

    @Autowired
    private AdoptionPetRepository adoptionPetRepository;

    public List<AdoptionPet> getAvailablePets() {
        List<AdoptionPet> availablePets = adoptionPetRepository.findByStatus(AdoptionStatus.Available); // Fixed method call
        System.out.println("Available pets from repository: " + availablePets); // Debugging
        return availablePets;
    }

    public AdoptionPet getPetById(int id) {
        AdoptionPet pet = adoptionPetRepository.findById(id).orElse(null);
        System.out.println("Pet by id from repository: " + pet); // Debugging
        return pet;
    }

    public void savePet(AdoptionPet pet) {
        adoptionPetRepository.save(pet);
    }
}
