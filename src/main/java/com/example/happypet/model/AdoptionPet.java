package com.example.happypet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdoptionPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pet_id;

    private String pet_name;
    private int pet_age;
    private String pet_species;
    private String pet_breed;

    @Enumerated(EnumType.STRING)
    private AdoptionPetGender pet_gender;

    @Column(columnDefinition = "TEXT")
    private String pet_description;


    private String imagePath;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    // Add this getter to expose imagePath as image_url
    public String getImage_url() {
        return imagePath;
    }

    public boolean isAdopted() {
        return false;
    }
}

