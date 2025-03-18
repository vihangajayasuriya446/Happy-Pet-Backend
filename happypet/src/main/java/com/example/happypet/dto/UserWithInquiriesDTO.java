package com.example.happypet.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithInquiriesDTO {
    private Long userId;
    private String name;
    private String email;
    private String contactNo;
    private String address;
    private String message;
    private String registrationDate;
    private String status;
    private List<PetInquiryDTO> interestedPets;
}

