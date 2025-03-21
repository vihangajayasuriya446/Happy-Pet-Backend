package com.example.happypet.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    // Set of allowed file extensions for security
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp")
    );


    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${file.upload-dir:${user.home}/happypet/uploads/}")
    private String uploadDirectory;

    private Path fileStoragePath;

    @PostConstruct
    public void init() {
        this.fileStoragePath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        logger.info("Initializing file storage at: {}", fileStoragePath);

        try {
            Files.createDirectories(this.fileStoragePath);
            logger.info("File storage directory created successfully");
        } catch (IOException e) {
            logger.error("Failed to create directory for file uploads", e);
            throw new RuntimeException("Could not create the directory for file uploads", e);
        }
    }

    /**
     * Saves an uploaded image file with validation and security checks
     */
    public String saveImage(MultipartFile file) throws IOException {
        // Validate file
        validateFile(file);

        // Clean the filename
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        logger.debug("Processing file upload: {}", originalFileName);

        // Extract file extension
        String fileExtension = getFileExtension(originalFileName);

        // Validate file extension
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            logger.warn("Rejected file with invalid extension: {}", fileExtension);
            throw new IOException("Only image files (jpg, jpeg, png, gif, bmp, webp) are allowed");
        }

        // Generate unique filename
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        logger.debug("Generated new filename: {}", newFileName);

        // Save file
        Path targetLocation = this.fileStoragePath.resolve(newFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        logger.info("Successfully saved file: {} (original: {})", newFileName, originalFileName);

        return newFileName;
    }

    /**
     * Retrieves an image file with enhanced error handling and fallback options
     */
    public byte[] getImage(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            logger.error("Attempted to retrieve image with null or empty filename");
            throw new IOException("Filename cannot be null or empty");
        }

        logger.debug("Attempting to retrieve image: {}", fileName);
        Path imagePath = this.fileStoragePath.resolve(fileName).normalize();

        // Security check to prevent directory traversal attacks
        if (!imagePath.toAbsolutePath().startsWith(fileStoragePath.toAbsolutePath())) {
            logger.warn("Security violation: Attempted to access file outside upload directory: {}", fileName);
            throw new IOException("Access denied. Cannot access file outside upload directory.");
        }

        if (!Files.exists(imagePath)) {
            logger.warn("Requested image not found: {}", fileName);

            // Try to find the file with different extensions
            String baseName = fileName;
            if (fileName.contains(".")) {
                baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            }

            for (String ext : ALLOWED_EXTENSIONS) {
                Path altPath = this.fileStoragePath.resolve(baseName + ext).normalize();
                if (Files.exists(altPath) &&
                        altPath.toAbsolutePath().startsWith(fileStoragePath.toAbsolutePath())) {
                    logger.info("Found alternative image with different extension: {}", baseName + ext);
                    return Files.readAllBytes(altPath);
                }
            }

            // If we get here, no alternative was found
            throw new IOException("File not found: " + fileName);
        }

        logger.debug("Successfully retrieved image: {}", fileName);
        return Files.readAllBytes(imagePath);
    }

    /**
     * Deletes an image file with enhanced error handling
     */
    public void deleteImage(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            logger.debug("Skipping delete for null or empty filename");
            return;
        }

        logger.debug("Attempting to delete image: {}", fileName);
        Path filePath = this.fileStoragePath.resolve(fileName).normalize();

        // Security check to prevent directory traversal attacks
        if (!filePath.toAbsolutePath().startsWith(fileStoragePath.toAbsolutePath())) {
            logger.warn("Security violation: Attempted to delete file outside upload directory: {}", fileName);
            throw new IOException("Access denied. Cannot delete file outside upload directory.");
        }

        boolean deleted = Files.deleteIfExists(filePath);
        if (deleted) {
            logger.info("Successfully deleted image: {}", fileName);
        } else {
            logger.warn("Image not found for deletion: {}", fileName);
        }
    }

    /**
     * Returns the upload directory path
     */
    public String getUploadDirectory() {
        return this.fileStoragePath.toString();
    }

    /**
     * Validates an uploaded file
     */
    private void validateFile(MultipartFile file) throws IOException {
        if (file == null) {
            logger.error("Null file provided for upload");
            throw new IOException("Failed to store null file");
        }

        if (file.isEmpty()) {
            logger.error("Empty file provided for upload");
            throw new IOException("Failed to store empty file");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            logger.warn("Rejected file exceeding maximum size: {} bytes", file.getSize());
            throw new IOException("File size exceeds the maximum allowed size (10MB)");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        // Check if the file's name contains invalid characters
        if (filename.contains("..")) {
            logger.warn("Security violation: Filename contains invalid path sequence: {}", filename);
            throw new IOException("Filename contains invalid path sequence: " + filename);
        }
    }

    /**
     * Extracts file extension from filename
     */
    private String getFileExtension(String filename) {
        String fileExtension = "";
        if (filename.contains(".")) {
            fileExtension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        }
        return fileExtension;
    }

    /**
     * Gets the MIME type based on file extension
     */
    public String getMimeType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();

        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".bmp":
                return "image/bmp";
            case ".webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Checks if a file exists in the storage
     */
    public boolean fileExists(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        Path filePath = this.fileStoragePath.resolve(fileName).normalize();
        return Files.exists(filePath) &&
                filePath.toAbsolutePath().startsWith(fileStoragePath.toAbsolutePath());
    }
}
