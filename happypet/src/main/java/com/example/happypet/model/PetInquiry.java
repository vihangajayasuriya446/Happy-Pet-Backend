package com.example.happypet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "pet_inquiries")
@Data
public class PetInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;
    private String userPhone;
    private String address;
    private String userMessage;

    private Long petId;
    private String petName;
    private String petType;
    private String petBreed;

    private LocalDateTime inquiryDate;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status = InquiryStatus.NEW;

    // Add enum for status
    public enum InquiryStatus {
        NEW, IN_PROGRESS, RESOLVED
    }
}
