package com.example.happypet.service;

import com.example.happypet.dto.BuyerDTO;
import com.example.happypet.model.Buyer;
import com.example.happypet.repo.BuyerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuyerService {

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<BuyerDTO> getAllBuyers() {
        List<Buyer> buyers = buyerRepo.findAll();
        return buyers.stream()
                .map(buyer -> modelMapper.map(buyer, BuyerDTO.class))
                .collect(Collectors.toList());
    }

    public BuyerDTO saveBuyer(BuyerDTO buyerDTO) {
        Buyer buyer = modelMapper.map(buyerDTO, Buyer.class);
        buyer = buyerRepo.save(buyer);
        return modelMapper.map(buyer, BuyerDTO.class);
    }

    public void deleteBuyer(Integer buyerId) {
        buyerRepo.deleteById(buyerId);
    }
}
