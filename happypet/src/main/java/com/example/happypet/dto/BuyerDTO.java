package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDTO {
    private int id;
    private String buyerName;
    private String contactNumber;
    private String deliveryMethod;
    private int petId;
}
