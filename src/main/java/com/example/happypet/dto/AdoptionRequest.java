package com.example.happypet.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdoptionRequest {
    // Getters and setters
    private int petId;
    private Integer userId; // Optional, can be null
    private String userName;
    private String email;
    private String phone;
    private String address;

}