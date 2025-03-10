package com.example.happypet.controller;

import com.example.happypet.dto.UserDTO;
import com.example.happypet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getusers")
    public List<UserDTO> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/adduser")
    public UserDTO saveUser(@RequestParam("id") int id,
                            @RequestParam("name") String name,
                            @RequestParam("type") String type,
                            @RequestParam("age") String age,
                            @RequestParam("gender") String gender,
                            @RequestParam("breed") String breed,
                            @RequestParam("location") String location,
                            @RequestParam("photo") MultipartFile photo)throws IOException {
        return userService.saveUser(id, name, type, age, gender, breed, location, photo);
    }

    @PutMapping("/updateuser")
    public UserDTO updateUser(@RequestParam("id") int id,
                              @RequestParam("name") String name,
                              @RequestParam("type") String type,
                              @RequestParam("age") String age,
                              @RequestParam("gender") String gender,
                              @RequestParam("breed") String breed,
                              @RequestParam("location") String location,
                              @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        return userService.updateUser(id, name, type, age, gender, breed, location, photo);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public void deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
    }

    @GetMapping("/getfetchedusers")
    public List<UserDTO> getFetchedUser(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String location) {
        return userService.getFetchedUser(type, age, gender, breed, location);
    }


}