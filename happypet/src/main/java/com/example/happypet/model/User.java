package com.example.happypet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    private int id;
    private String name;
    private String type;
    private String age;
    private String gender;
    private String breed;
    private String location;

    @Lob
    private byte[] photo;
}