package com.example.happypet.service;
import com.example.happypet.dto.CartDTO;
import com.example.happypet.dto.PetDTO;
import com.example.happypet.model.Cart;
import com.example.happypet.model.Pet;
import com.example.happypet.repo.CartRepo;
import com.example.happypet.repo.PetRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private PetRepo petRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${server.url:http://localhost:8080}") // Base URL for serving images
    private String serverUrl;

    @Value("${api.images.endpoint:/api/v1/pets/images/}") // Endpoint for accessing images
    private String imagesEndpoint;

    /**
     * Fetch all cart items and map them to CartDTO, including imageUrl for each pet.
     */
    public List<CartDTO> getAllCartItems() {
        List<Cart> cartItems = cartRepo.findAll();
        logger.info("Fetched {} cart items", cartItems.size());

        return cartItems.stream()
                .map(this::convertToCartDTO)
                .collect(Collectors.toList());
    }

    /**
     * Add a pet to the cart. If the pet already exists in the cart, update the quantity.
     */
    public CartDTO addToCart(Long petId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));

        logger.info("Adding pet with ID {} to cart, quantity: {}", petId, quantity);

        Cart cartItem = cartRepo.findByPetId(petId)
                .map(existingCart -> {
                    logger.info("Pet already in cart, updating quantity from {} to {}",
                            existingCart.getQuantity(), existingCart.getQuantity() + quantity);
                    existingCart.setQuantity(existingCart.getQuantity() + quantity);
                    return existingCart;
                })
                .orElseGet(() -> {
                    logger.info("Creating new cart item for pet ID: {}", petId);
                    Cart newCartItem = new Cart();
                    newCartItem.setPet(pet);
                    newCartItem.setQuantity(quantity);
                    return newCartItem;
                });

        cartItem = cartRepo.save(cartItem);
        return convertToCartDTO(cartItem);
    }

    /**
     * Update the quantity of a cart item. If the quantity is zero or less, remove the item.
     */
    public CartDTO updateCartItemQuantity(Long cartItemId, Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be zero or greater.");
        }

        Cart cartItem = cartRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (quantity == 0) {
            logger.info("Removing cart item with ID {} due to zero quantity", cartItemId);
            cartRepo.delete(cartItem);
            return null; // Return null to indicate the item was removed
        }

        logger.info("Updating cart item {} quantity to {}", cartItemId, quantity);
        cartItem.setQuantity(quantity);
        cartItem = cartRepo.save(cartItem);

        return convertToCartDTO(cartItem);
    }

    /**
     * Remove a specific item from the cart by its ID.
     */
    public void removeFromCart(Long cartItemId) {
        if (!cartRepo.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found with id: " + cartItemId);
        }
        logger.info("Removing cart item with ID: {}", cartItemId);
        cartRepo.deleteById(cartItemId);
    }

    /**
     * Clear all items from the cart.
     */
    public void clearCart() {
        logger.info("Clearing all items from cart");
        cartRepo.deleteAll();
    }

    /**
     * Calculate the total cost of all items in the cart.
     */
    public double calculateCartTotal() {
        double total = cartRepo.findAll().stream()
                .mapToDouble(cart -> cart.getPet().getPrice() * cart.getQuantity())
                .sum();
        logger.info("Calculated cart total: {}", total);
        return total;
    }

    /**
     * Convert a Cart entity to a CartDTO, including the imageUrl for the pet.
     */
    private CartDTO convertToCartDTO(Cart cart) {
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        // Convert Pet to PetDTO and include imageUrl
        Pet pet = cart.getPet();
        PetDTO petDTO = modelMapper.map(pet, PetDTO.class);

        // Add the imageUrl to the PetDTO
        if (pet.getImageUrl() != null && !pet.getImageUrl().trim().isEmpty()) {
            String imageUrl = pet.getImageUrl();

            // Log the original image URL for debugging
            logger.debug("Original pet image URL: {}", imageUrl);

            // If the image URL already starts with http:// or https://, use it as is
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                petDTO.setImageUrl(imageUrl);
            }
            // If the URL already contains the API endpoint path
            else if (imageUrl.contains("/api/v1/pets/images/")) {
                // Extract just the filename
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                // Reconstruct the URL properly
                petDTO.setImageUrl(serverUrl + imagesEndpoint + fileName);
            }
            // If the URL is just a filename
            else {
                // Ensure we don't have double slashes in the URL
                String endpoint = imagesEndpoint;
                if (endpoint.endsWith("/") && imageUrl.startsWith("/")) {
                    endpoint = endpoint.substring(0, endpoint.length() - 1);
                }

                // Remove any leading slash from the image URL
                if (imageUrl.startsWith("/")) {
                    imageUrl = imageUrl.substring(1);
                }

                petDTO.setImageUrl(serverUrl + endpoint + imageUrl);
            }

            // Log the final image URL for debugging
            logger.debug("Final pet image URL: {}", petDTO.getImageUrl());
        } else {
            logger.warn("Pet with ID {} has no image URL", pet.getId());
        }

        // Calculate subtotal
        double subtotal = pet.getPrice() * cart.getQuantity();
        cartDTO.setSubtotal(subtotal);

        cartDTO.setPet(petDTO);
        return cartDTO;
    }
}
