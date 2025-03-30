package com.example.happypet.dto;

import lombok.Data;

@Data
public class ContactFormDTO {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String status;
}