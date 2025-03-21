package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    @NotBlank(message = "User email cannot be empty or null")
    @Email(message = "Invalid user email address")
    private String email;
    @NotBlank(message = "User name cannot be empty or null")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]+$", message = "Invalid user name")
    private String name;
    @NotBlank(message = "User address cannot be empty or null")
    @Pattern(regexp = "^[A-Za-z\\d][A-Za-z\\d-|/# ,.:;\\\\]+$", message = "Invalid user address")
    private String address;
    @NotBlank(message = "User contact number cannot be empty or null")
    @Pattern(regexp = "^\\d{3}-\\d{7}$", message = "Invalid user contact number")
    private String contact;
}
