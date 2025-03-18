package com.example.happypet.repo;
import com.example.happypet.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByPetId(Long petId);
    // List<CartItem> findByUserId(String userId);
}
