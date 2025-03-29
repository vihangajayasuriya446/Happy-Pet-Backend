package com.example.happypet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AdoptionImageStorageService {

    @Value("${file.upload-dir:src/main/resources/static/images}")
    private String uploadDir;

    public String storeImage(MultipartFile file) throws IOException {
        // Create the upload directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate a unique filename
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(directory.getAbsolutePath(), filename);

        // Save the file to the server
        Files.copy(file.getInputStream(), filePath);

        // Construct the image URL
        String imageUrl = "/images/" + filename;
        System.out.println("Generated image URL: " + imageUrl);

        return imageUrl;
    }
}

