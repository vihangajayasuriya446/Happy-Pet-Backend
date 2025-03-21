package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private Long id;
    private String name;
    private String petType;
    private Double price;
    private String breed;
    private String birthYear;
    private String gender;
    private MultipartFile image;
    private String imageUrl;
    private boolean purchased;

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPetType() {
        return petType;
    }

    public Double getPrice() {
        return price;
    }

    public String getBreed() {
        return breed;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getGender() {
        return gender;
    }

    public MultipartFile getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isPurchased() {
        return purchased;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}
