package com.example.happypet.repo;

import com.example.happypet.model.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Integer> {
    List<Adoption> findByPetId(int petId);
}

