package com.example.happypet.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String petType;
    private Double price;
    private String breed;
    private String birthYear;
    private String gender;
    private String imageUrl;
    private boolean purchased = false;
}
