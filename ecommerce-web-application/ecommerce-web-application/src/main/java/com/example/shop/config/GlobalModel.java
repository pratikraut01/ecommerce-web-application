package com.example.shop.config;

import com.example.shop.repository.CartItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Makes the cart badge count available to every page. */
@ControllerAdvice
public class GlobalModel {
    private final CartItemRepository cartRepo;
    public GlobalModel(CartItemRepository cartRepo) { this.cartRepo = cartRepo; }

    @ModelAttribute("cartCount")
    public long cartCount(HttpSession s) {
        Long uid = (Long) s.getAttribute("uid");
        return uid == null ? 0 : cartRepo.countByUserId(uid);
    }
}
