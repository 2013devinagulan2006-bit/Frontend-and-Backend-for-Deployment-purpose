package com.devimart.backend.repository;

import com.devimart.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByProductName(String productName);
}