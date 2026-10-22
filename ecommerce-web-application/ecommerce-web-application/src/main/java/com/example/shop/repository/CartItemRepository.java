package com.example.shop.repository;

import com.example.shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserIdOrderByIdAsc(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    long countByUserId(Long userId);
    void deleteByUserId(Long userId);
}
