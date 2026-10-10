package com.devimart.backend.controller;

import com.devimart.backend.entity.Cart;
import com.devimart.backend.repository.CartRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public List<Cart> getCart() {
        return cartRepository.findAll();
    }

    @PostMapping
    public Cart addToCart(@RequestBody Cart cart) {

        System.out.println("POST /api/cart received: " + cart.getProductName());

        Cart existingCart =
                cartRepository.findByProductName(cart.getProductName());

        if (existingCart != null) {

            int newQuantity =
                    existingCart.getQuantity() + cart.getQuantity();

            existingCart.setQuantity(newQuantity);

            existingCart.setTotalPrice(
                    existingCart.getPrice() * newQuantity
            );

            return cartRepository.saveAndFlush(existingCart);
        }

        cart.setTotalPrice(
                cart.getPrice() * cart.getQuantity()
        );

        return cartRepository.saveAndFlush(cart);
    }

    @PutMapping("/{id}")
    public Cart updateQuantity(
            @PathVariable Long id,
            @RequestBody Cart updatedCart) {

        Cart cart =
                cartRepository.findById(id).orElse(null);

        if (cart == null) {
            return null;
        }

        int quantity = updatedCart.getQuantity();

        if (quantity < 1) {
            quantity = 1;
        }

        cart.setQuantity(quantity);

        cart.setTotalPrice(
                cart.getPrice() * quantity
        );

        return cartRepository.saveAndFlush(cart);
    }

    @DeleteMapping("/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartRepository.deleteById(id);
    }

    @DeleteMapping
    public void clearCart() {
        cartRepository.deleteAll();
    }
}