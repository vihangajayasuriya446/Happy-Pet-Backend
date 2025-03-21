package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private int id;
    private String name;
    private String type;
    private String age;
    private String gender;
    private String breed;
    private String location;

    private String photo;
}
