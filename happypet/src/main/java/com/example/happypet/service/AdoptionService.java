package com.example.happypet.service;


import com.example.happypet.model.Adoption;
import com.example.happypet.dto.AdoptionRequest;
import com.example.happypet.model.AdoptionStatus;
import com.example.happypet.repo.AdoptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.happypet.model.AdoptionPet;
import com.example.happypet.repo.AdoptionPetRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private AdoptionPetRepository petRepository;

    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    public Adoption getAdoptionById(int id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }

    public List<Adoption> getAdoptionsByPetId(int petId) {
        return adoptionRepository.findByPetId(petId);
    }

    public Adoption submitAdoption(AdoptionRequest request) {
        Adoption adoption = new Adoption();
        adoption.setPetId(request.getPetId());
        AdoptionPet pet = petRepository.findById(request.getPetId()).orElse(null);
        if (pet != null) {
            adoption.setPet_name(((AdoptionPet) pet).getPet_name());
            pet.setStatus(AdoptionStatus.Pending); // update pet status to pending
            petRepository.save(pet);
        }
        if (request.getUserId() != null) {
            adoption.setUserId(request.getUserId());
        }
        adoption.setUserName(request.getUserName());
        adoption.setEmail(request.getEmail());
        adoption.setPhone(request.getPhone());
        adoption.setAddress(request.getAddress());
        adoption.setStatus("Pending");
        adoption.setApplicationDate(LocalDateTime.now()); // Set the application date here
        adoptionRepository.save(adoption);

        return adoption;
    }


    public Adoption updateAdoptionStatus(int id, Adoption adoptionUpdate) {
        Adoption adoption = getAdoptionById(id);
        adoption.setStatus(adoptionUpdate.getStatus());
        AdoptionPet pet = petRepository.findById(adoption.getPetId()).orElse(null);
        if(pet !=null){
            if ("Approved".equals(adoptionUpdate.getStatus())) {
                pet.setStatus(AdoptionStatus.Approved);
            } else if ("Rejected".equals(adoptionUpdate.getStatus())) {
                pet.setStatus(AdoptionStatus.Available);
            }
            petRepository.save(pet);
        }
        return adoptionRepository.save(adoption);
    }


    public void deleteAdoption(int id) {
        Adoption adoption = getAdoptionById(id);
        adoptionRepository.delete(adoption);
    }
}
