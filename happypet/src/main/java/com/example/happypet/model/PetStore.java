package com.example.happypet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private String age;
    private double price;
    private String status; // e.g., "available", "adopted", "pending"

    @Lob
    private byte[] photo;
}
