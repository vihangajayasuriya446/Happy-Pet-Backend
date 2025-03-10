package com.example.happypet.service;

import com.example.happypet.dto.OwnerDTO;
import com.example.happypet.model.Owner;
import com.example.happypet.repo.OwnerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OwnerService {

    @Autowired
    private OwnerRepo ownerRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<OwnerDTO> getAllOwners() {
        List<Owner> owners = ownerRepo.findAll();
        return owners.stream()
                .map(owner -> modelMapper.map(owner, OwnerDTO.class))
                .collect(Collectors.toList());
    }

    public OwnerDTO saveOwner(OwnerDTO ownerDTO) {
        Owner owner = modelMapper.map(ownerDTO, Owner.class);
        owner = ownerRepo.save(owner);
        return modelMapper.map(owner, OwnerDTO.class);
    }

    public OwnerDTO updateOwner(int id, OwnerDTO ownerDTO) {
        Optional<Owner> existingOwnerOptional = ownerRepo.findById(id);
        if (existingOwnerOptional.isPresent()) {
            Owner existingOwner = existingOwnerOptional.get();
            existingOwner.setOwnerName(ownerDTO.getOwnerName());
            existingOwner.setAddress(ownerDTO.getAddress());
            existingOwner.setContactNumber(ownerDTO.getContactNumber());

            return modelMapper.map(ownerRepo.save(existingOwner), OwnerDTO.class);
        } else {
            throw new RuntimeException("Owner with ID " + id + " not found.");
        }
    }

    public void deleteOwner(Integer ownerId) {
        ownerRepo.deleteById(ownerId);
    }
}
