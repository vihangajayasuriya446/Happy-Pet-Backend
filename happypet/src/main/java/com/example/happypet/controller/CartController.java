package com.example.happypet.controller;
import com.example.happypet.dto.CartDTO;
import com.example.happypet.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDTO>> getCart() {
        return ResponseEntity.ok(cartService.getAllCartItems());
    }

    @PostMapping("/add/{petId}")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable Long petId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        CartDTO cartItem = cartService.addToCart(petId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        CartDTO updatedItem = cartService.updateCartItemQuantity(cartItemId, quantity);
        if (updatedItem == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getCartTotal() {
        double total = cartService.calculateCartTotal();
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout() {


        double total = cartService.calculateCartTotal();
        cartService.clearCart();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Checkout successful");
        response.put("total", total);

        return ResponseEntity.ok(response);
    }
}
