package com.example.happypet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "contact_form")
public class ContactForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column // You can specify length if needed
    private String status;

}
