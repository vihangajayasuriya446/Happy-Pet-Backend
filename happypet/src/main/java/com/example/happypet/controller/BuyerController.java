package com.example.happypet.controller;

import com.example.happypet.dto.BuyerDTO;
import com.example.happypet.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//
@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class BuyerController {
    @Autowired
    private BuyerService buyerService;

    @GetMapping("/getbuyers")
    public List<BuyerDTO> getAllBuyers() {
        return buyerService.getAllBuyers();
    }

    @PostMapping("/addbuyer")
    public BuyerDTO saveBuyer(@RequestBody BuyerDTO buyerDTO) {
        return buyerService.saveBuyer(buyerDTO);
    }


    

    @DeleteMapping("/deletebuyer/{buyerId}")
    public void deleteBuyer(@PathVariable Integer buyerId) {
        buyerService.deleteBuyer(buyerId);
    }
}
