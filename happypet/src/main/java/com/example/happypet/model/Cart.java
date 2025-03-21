package com.example.happypet.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private Integer quantity;


    // Calculate subtotal
    @Transient
    public Double getSubtotal() {
        if (pet != null && pet.getPrice() != null && quantity != null) {
            return pet.getPrice() * quantity;
        }
        return 0.0;
    }
}
