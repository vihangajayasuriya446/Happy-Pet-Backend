package com.example.happypet.repo;

import com.example.happypet.model.AdoptionStatus;
import com.example.happypet.model.AdoptionPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdoptionPetRepository extends JpaRepository<AdoptionPet, Integer> {
    List<AdoptionPet> findByStatus(AdoptionStatus status);
}
