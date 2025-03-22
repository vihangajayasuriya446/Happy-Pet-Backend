package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetStoreDTO {
    private int id;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private String age;
    private double price;
    private String status;
    private String photo; // Will hold Base64 encoded image
}
