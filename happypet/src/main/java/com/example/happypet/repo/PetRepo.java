package com.example.happypet.repo;
import com.example.happypet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepo extends JpaRepository<Pet, Long> {
    List<Pet> findByPetType(String petType);
    List<Pet> findByNameContainingIgnoreCase(String name);
    List<Pet> findByGenderIgnoreCase(String gender);
    List<Pet> findByBreedContainingIgnoreCase(String breed);
}
