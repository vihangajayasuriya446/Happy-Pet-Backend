package com.example.happypet.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetInquiryDTO {
    private Long petId;
    private String name;
    private String category;
    private String breed;
    private Double price;
    private String age;
    private String imageUrl;
    private String inquiryDate;
    private String message;
    private String description;
    private String status;
}
