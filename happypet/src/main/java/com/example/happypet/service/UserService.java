package com.example.happypet.service;

import com.example.happypet.dto.UserDTO;
import com.example.happypet.model.User;
import com.example.happypet.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();

        return users.stream().map(user -> {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            if (user.getPhoto() != null) {
                userDTO.setPhoto(Base64.getEncoder().encodeToString(user.getPhoto())); // Convert byte[] to Base64 string
            }
            return userDTO;
        }).collect(Collectors.toList());
    }

    public UserDTO saveUser(int id, String name, String type, String age, String gender, String breed, String location, MultipartFile photo) throws IOException {
        if (!photo.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
        }

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setType(type);
        user.setAge(age);
        user.setGender(gender);
        user.setBreed(breed);
        user.setLocation(location);
        user.setPhoto(photo.getBytes()); // Convert image to byte array

        userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO updateUser(int id, String name, String type, String age, String gender, String breed, String location, MultipartFile photo) throws IOException {
        Optional<User> existingUserOptional = userRepo.findById(id);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            user.setName(name);
            user.setType(type);
            user.setAge(age);
            user.setGender(gender);
            user.setBreed(breed);
            user.setLocation(location);

            // Update the photo if a new one is provided
            if (photo != null && !photo.isEmpty()) {
                if (!photo.getContentType().startsWith("image")) {
                    throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
                }
                user.setPhoto(photo.getBytes());
            }

            userRepo.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else {
            throw new RuntimeException("User with ID " + id + " not found.");
        }
    }


    public String deleteUser(Integer userId){
        userRepo.deleteById(userId);
        return "User successfully deleted";
    }
}