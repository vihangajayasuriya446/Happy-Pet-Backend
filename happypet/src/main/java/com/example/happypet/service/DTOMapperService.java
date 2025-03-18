package com.example.happypet.service;
import com.example.happypet.dto.PetInquiryDTO;
import com.example.happypet.model.Pet;
import com.example.happypet.model.PetInquiry;
import com.example.happypet.repo.PetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DTOMapperService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    private PetRepo petRepo;

    public PetInquiryDTO convertToPetInquiryDTO(PetInquiry inquiry) {
        PetInquiryDTO dto = new PetInquiryDTO();
        dto.setPetId(inquiry.getPetId());
        dto.setName(inquiry.getPetName());
        dto.setCategory(inquiry.getPetType());
        dto.setBreed(inquiry.getPetBreed());

        // Fetch additional pet details from the repository if petId is available
        if (inquiry.getPetId() != null) {
            Optional<Pet> petOptional = petRepo.findById(inquiry.getPetId());
            if (petOptional.isPresent()) {
                Pet pet = petOptional.get();
                dto.setPrice(pet.getPrice());
                dto.setAge(pet.getBirthYear());
                dto.setImageUrl(pet.getImageUrl());
            }
        }

        dto.setInquiryDate(inquiry.getInquiryDate() != null ?
                inquiry.getInquiryDate().format(DATE_FORMATTER) : "");
        dto.setMessage(inquiry.getUserMessage());
        dto.setDescription("Inquiry about " + inquiry.getPetName());

        return dto;
    }

    public List<PetInquiryDTO> convertToPetInquiryDTOList(List<PetInquiry> inquiries) {
        return inquiries.stream()
                .map(this::convertToPetInquiryDTO)
                .collect(Collectors.toList());
    }
}
