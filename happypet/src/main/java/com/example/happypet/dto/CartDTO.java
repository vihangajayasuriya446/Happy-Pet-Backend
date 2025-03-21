package com.example.happypet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long id;
    private PetDTO pet;
    private Integer quantity;
    private Double subtotal;
}
