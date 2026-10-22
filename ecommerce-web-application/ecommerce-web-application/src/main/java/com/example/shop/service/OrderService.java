package com.example.shop.service;

import com.example.shop.model.*;
import com.example.shop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final CartItemRepository cartRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public OrderService(CartItemRepository cartRepo, OrderRepository orderRepo, UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    /** Converts the user's cart into an order + payment record in ONE transaction. */
    @Transactional
    public CustomerOrder checkout(Long userId, String address, String method) {
        List<CartItem> cart = cartRepo.findByUserIdOrderByIdAsc(userId);
        if (cart.isEmpty()) throw new IllegalStateException("Your cart is empty");

        CustomerOrder order = new CustomerOrder();
        order.setUser(userRepo.getReferenceById(userId));
        order.setShippingAddress(address);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart) {
            Product p = ci.getProduct();
            if (!p.isActive()) throw new IllegalStateException(p.getName() + " is no longer available");
            if (p.getStock() < ci.getQuantity())
                throw new IllegalStateException("Only " + p.getStock() + " left of " + p.getName());
            p.setStock(p.getStock() - ci.getQuantity());           // reduce stock (dirty checking saves it)

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(p.getPrice());
            order.getItems().add(oi);
            total = total.add(oi.getSubtotal());
        }
        order.setTotalAmount(total);

        Payment pay = new Payment();
        pay.setOrder(order);
        pay.setMethod(method);
        pay.setAmount(total);
        pay.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        boolean online = !"COD".equals(method);                   // simulated gateway
        pay.setStatus(online ? "PAID" : "PENDING");
        if (online) pay.setPaidAt(LocalDateTime.now());
        order.setPayment(pay);

        CustomerOrder saved = orderRepo.save(order);
        cartRepo.deleteByUserId(userId);
        return saved;
    }
}
