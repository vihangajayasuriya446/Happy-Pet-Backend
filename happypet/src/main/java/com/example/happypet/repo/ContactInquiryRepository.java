package com.example.happypet.repo;

import com.example.happypet.model.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {
    // Add custom query methods if needed
}
