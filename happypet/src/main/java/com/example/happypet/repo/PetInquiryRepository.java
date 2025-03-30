package com.example.happypet.repo;

import com.example.happypet.model.PetInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetInquiryRepository extends JpaRepository<PetInquiry, Long> {
    List<PetInquiry> findByUserEmail(String email);
    List<PetInquiry> findByPetId(Long petId);
}

