package com.example.shop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class CustomerOrder {
    public static final List<String> STATUSES = List.of("PLACED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false) private String status = "PLACED";
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal totalAmount;
    @Column(nullable = false, length = 500) private String shippingAddress;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
}
