package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDTO {
    private int id;
    private String ownerName;
    private String address;
    private String contactNumber;
    // Add other relevant fields as needed
}

