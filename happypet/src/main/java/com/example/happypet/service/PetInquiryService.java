package com.example.happypet.service;

import com.example.happypet.dto.PetInquiryDTO;
import com.example.happypet.dto.UserWithInquiriesDTO;
import com.example.happypet.model.Pet;
import com.example.happypet.model.PetInquiry;
import com.example.happypet.repo.PetInquiryRepository;
import com.example.happypet.repo.PetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetInquiryService {
    private static final Logger logger = LoggerFactory.getLogger(PetInquiryService.class);

    @Autowired
    private PetInquiryRepository petInquiryRepository;

    @Autowired
    private PetRepo petRepo;

    @Autowired
    private DTOMapperService dtoMapperService;

    // Get all inquiries
    public List<PetInquiry> getAllInquiries() {
        return petInquiryRepository.findAll();
    }

    // Get inquiry by ID
    public PetInquiry getInquiryById(Long id) {
        logger.info("Getting inquiry with ID: {}", id);
        return petInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found with id: " + id));
    }

    // Create new inquiry
    public PetInquiry createInquiry(PetInquiry inquiry) {
        logger.info("Creating new inquiry for pet ID: {}", inquiry.getPetId());

        // Set inquiry date if not provided
        if (inquiry.getInquiryDate() == null) {
            inquiry.setInquiryDate(LocalDateTime.now());
        }

        // Set default status if not provided
        if (inquiry.getStatus() == null) {
            inquiry.setStatus(PetInquiry.InquiryStatus.NEW);
        }

        return petInquiryRepository.save(inquiry);
    }

    // Update existing inquiry
    public PetInquiry updateInquiry(Long id, PetInquiry inquiryDetails) {
        logger.info("Updating inquiry with ID: {}", id);

        PetInquiry inquiry = getInquiryById(id);

        // Update fields
        inquiry.setUserName(inquiryDetails.getUserName());
        inquiry.setUserEmail(inquiryDetails.getUserEmail());
        inquiry.setUserPhone(inquiryDetails.getUserPhone());
        inquiry.setAddress(inquiryDetails.getAddress());
        inquiry.setUserMessage(inquiryDetails.getUserMessage());
        inquiry.setPetId(inquiryDetails.getPetId());
        inquiry.setPetName(inquiryDetails.getPetName());
        inquiry.setPetType(inquiryDetails.getPetType());
        inquiry.setPetBreed(inquiryDetails.getPetBreed());
        inquiry.setStatus(inquiryDetails.getStatus());

        return petInquiryRepository.save(inquiry);
    }

    // Delete inquiry
    public void deleteInquiry(Long id) {
        logger.info("Deleting inquiry with ID: {}", id);

        PetInquiry inquiry = getInquiryById(id);
        petInquiryRepository.delete(inquiry);
    }

    // Update inquiry status
    public PetInquiry updateInquiryStatus(Long id, PetInquiry.InquiryStatus status) {
        logger.info("Updating status for inquiry ID: {} to {}", id, status);

        PetInquiry inquiry = getInquiryById(id);
        inquiry.setStatus(status);

        return petInquiryRepository.save(inquiry);
    }

    // Get inquiries by email
    public List<PetInquiry> getInquiriesByEmail(String email) {
        logger.info("Getting inquiries for email: {}", email);
        return petInquiryRepository.findByUserEmail(email);
    }

    // Get inquiries by pet ID
    public List<PetInquiry> getInquiriesByPetId(Long petId) {
        logger.info("Getting inquiries for pet ID: {}", petId);
        return petInquiryRepository.findByPetId(petId);
    }

    // Get dashboard data (raw)
    public List<PetInquiry> getDashboardData() {
        logger.info("Getting raw dashboard data");
        return getAllInquiries();
    }

    // Enhanced method for dashboard data that returns data in the format expected by the frontend
    public List<UserWithInquiriesDTO> getDashboardDataFormatted() {
        logger.info("Starting getDashboardDataFormatted method");

        // Get all inquiries
        List<PetInquiry> allInquiries = getAllInquiries();
        logger.info("Retrieved {} total inquiries from database", allInquiries.size());

        if (allInquiries.isEmpty()) {
            logger.warn("No inquiries found in the database");
            return new ArrayList<>();
        }

        // Group inquiries by user email
        Map<String, List<PetInquiry>> inquiriesByUser = allInquiries.stream()
                .collect(Collectors.groupingBy(PetInquiry::getUserEmail));

        logger.info("Grouped inquiries by user email, found {} unique users", inquiriesByUser.size());

        List<UserWithInquiriesDTO> result = new ArrayList<>();

        // For each user, create a UserWithInquiriesDTO
        inquiriesByUser.forEach((email, inquiries) -> {
            if (!inquiries.isEmpty()) {
                PetInquiry firstInquiry = inquiries.get(0);

                logger.debug("Processing user: {} with {} inquiries", email, inquiries.size());

                UserWithInquiriesDTO userDTO = new UserWithInquiriesDTO();
                // Use the actual ID from the inquiry if possible, otherwise generate one
                userDTO.setUserId(firstInquiry.getId() != null ? firstInquiry.getId() : (long) result.size() + 1);
                userDTO.setName(firstInquiry.getUserName());
                userDTO.setEmail(firstInquiry.getUserEmail());
                userDTO.setContactNo(firstInquiry.getUserPhone());
                userDTO.setAddress(firstInquiry.getAddress());
                userDTO.setMessage(firstInquiry.getUserMessage());

                // Format date safely
                String formattedDate = "";
                if (firstInquiry.getInquiryDate() != null) {
                    try {
                        formattedDate = firstInquiry.getInquiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (Exception e) {
                        logger.error("Error formatting date for user {}: {}", email, e.getMessage());
                        formattedDate = firstInquiry.getInquiryDate().toString();
                    }
                }
                userDTO.setRegistrationDate(formattedDate);

                // Set status safely
                userDTO.setStatus(firstInquiry.getStatus() != null ? firstInquiry.getStatus().toString() : "NEW");

                // Use the DTOMapperService to convert inquiries to DTOs
                try {
                    List<PetInquiryDTO> petDTOs = dtoMapperService.convertToPetInquiryDTOList(inquiries);
                    for (int i = 0; i < petDTOs.size(); i++) {
                        PetInquiry inquiry = inquiries.get(i);
                        petDTOs.get(i).setStatus(inquiry.getStatus() != null ? inquiry.getStatus().toString() : "NEW");
                    }
                    userDTO.setInterestedPets(petDTOs);

                    logger.debug("Successfully mapped {} pet inquiries for user {}", petDTOs.size(), email);
                } catch (Exception e) {
                    logger.error("Error converting inquiries to DTOs for user {}: {}", email, e.getMessage(), e);

                    // Fallback to manual conversion if the mapper service fails
                    List<PetInquiryDTO> fallbackDTOs = new ArrayList<>();
                    for (PetInquiry inquiry : inquiries) {
                        try {
                            PetInquiryDTO dto = new PetInquiryDTO();
                            dto.setPetId(inquiry.getPetId());
                            dto.setName(inquiry.getPetName() != null ? inquiry.getPetName() : "Unknown Pet");
                            dto.setCategory(inquiry.getPetType() != null ? inquiry.getPetType() : "Unknown Type");
                            dto.setBreed(inquiry.getPetBreed() != null ? inquiry.getPetBreed() : "Unknown Breed");
                            dto.setMessage(inquiry.getUserMessage());

                            // Format inquiry date safely
                            String inquiryDate = "";
                            if (inquiry.getInquiryDate() != null) {
                                try {
                                    inquiryDate = inquiry.getInquiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                } catch (Exception ex) {
                                    logger.error("Error formatting inquiry date: {}", ex.getMessage());
                                    inquiryDate = inquiry.getInquiryDate().toString();
                                }
                            }
                            dto.setInquiryDate(inquiryDate);
                            dto.setDescription("Inquiry about " + (inquiry.getPetName() != null ? inquiry.getPetName() : "a pet"));
                            dto.setStatus(inquiry.getStatus() != null ? inquiry.getStatus().toString() : "NEW");
                            // Enhance with pet details if available
                            if (inquiry.getPetId() != null) {
                                try {
                                    Optional<Pet> petOptional = petRepo.findById(inquiry.getPetId());
                                    if (petOptional.isPresent()) {
                                        Pet pet = petOptional.get();
                                        dto.setPrice(pet.getPrice());
                                        dto.setAge(pet.getBirthYear());
                                        dto.setImageUrl(pet.getImageUrl());
                                    } else {
                                        dto.setPrice(0.0);
                                        dto.setAge("Unknown");
                                        dto.setImageUrl("");
                                    }
                                } catch (Exception ex) {
                                    logger.error("Error retrieving pet with ID {}: {}", inquiry.getPetId(), ex.getMessage());
                                    dto.setPrice(0.0);
                                    dto.setAge("Error");
                                    dto.setImageUrl("");
                                }
                            } else {
                                dto.setPrice(0.0);
                                dto.setAge("N/A");
                                dto.setImageUrl("");
                            }

                            fallbackDTOs.add(dto);
                        } catch (Exception ex) {
                            logger.error("Error in fallback processing for inquiry: {}", ex.getMessage(), ex);
                        }
                    }
                    userDTO.setInterestedPets(fallbackDTOs);
                    logger.info("Used fallback conversion for user {} with {} inquiries", email, fallbackDTOs.size());
                }

                result.add(userDTO);
            }
        });

        logger.info("Returning {} users with their inquiries", result.size());

        // Log first user details for debugging if available
        if (!result.isEmpty()) {
            UserWithInquiriesDTO firstUser = result.get(0);
            logger.debug("First user in result: id={}, name={}, email={}, pets={}",
                    firstUser.getUserId(),
                    firstUser.getName(),
                    firstUser.getEmail(),
                    firstUser.getInterestedPets() != null ? firstUser.getInterestedPets().size() : 0);
        }

        return result;
    }
}
