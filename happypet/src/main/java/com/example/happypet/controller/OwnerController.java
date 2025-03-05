package com.example.happypet.controller;

import com.example.happypet.dto.OwnerDTO;
import com.example.happypet.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/getowners")
    public List<OwnerDTO> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @PostMapping("/addowner")
    public OwnerDTO saveOwner(@RequestBody OwnerDTO ownerDTO) {
        return ownerService.saveOwner(ownerDTO);
    }

    @PutMapping("/updateowner/{id}")
    public OwnerDTO updateOwner(@PathVariable int id, @RequestBody OwnerDTO ownerDTO) {
        return ownerService.updateOwner(id, ownerDTO);
    }

    @DeleteMapping("/deleteowner/{ownerId}")
    public void deleteOwner(@PathVariable Integer ownerId) {
        ownerService.deleteOwner(ownerId);
    }
}
