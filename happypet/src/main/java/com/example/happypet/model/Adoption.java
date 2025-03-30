package com.example.happypet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "adoptions")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adoption_id;

    @Setter
    @Getter
    @Column(name = "pet_id")
    private int petId;

    private Integer user_id;
    private String user_name;

    @Setter
    @Getter
    private String email;

    @Getter
    @Setter
    private String phone;

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    private String status;

    @Setter
    @Getter
    private String pet_name;

    @Getter // Added getter for applicationDate
    @Setter
    @Column(name = "application_date", updatable = false) // Prevent manual updates
    private LocalDateTime application_date;

    // Getters and setters
    public int getAdoptionId() {
        return adoption_id;
    }

    public void setAdoptionId(int adoption_id) {
        this.adoption_id = adoption_id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public LocalDateTime getApplicationDate() {
        return application_date;
    }

    public void setApplicationDate(LocalDateTime application_date) {
        this.application_date = application_date;
    }
}

