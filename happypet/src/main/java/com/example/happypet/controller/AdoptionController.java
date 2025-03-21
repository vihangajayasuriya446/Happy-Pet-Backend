package com.example.happypet.controller;


import com.example.happypet.model.Adoption;
import com.example.happypet.dto.AdoptionRequest;
import com.example.happypet.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @GetMapping
    public List<Adoption> getAllAdoptions() {
        return adoptionService.getAllAdoptions();
    }

    @GetMapping("/{id}")
    public Adoption getAdoptionById(@PathVariable int id) {
        return adoptionService.getAdoptionById(id);
    }

    @GetMapping("/pet/{petId}")
    public List<Adoption> getAdoptionsByPetId(@PathVariable int petId) {
        return adoptionService.getAdoptionsByPetId(petId);
    }

    @PostMapping("/submit")
    public Adoption submitAdoption(@RequestBody AdoptionRequest request) {
        return adoptionService.submitAdoption(request);
    }

    @PutMapping("/update/{id}")
    public Adoption updateAdoptionStatus(@PathVariable int id, @RequestBody Adoption adoption) {
        return adoptionService.updateAdoptionStatus(id, adoption);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAdoption(@PathVariable int id) {
        adoptionService.deleteAdoption(id);
    }
}
