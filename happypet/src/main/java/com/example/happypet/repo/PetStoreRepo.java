package com.example.happypet.repo;

import com.example.happypet.model.PetStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetStoreRepo extends JpaRepository<PetStore, Integer> {
}
