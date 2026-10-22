package com.example.shop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "payments")
@Getter @Setter @NoArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false) @JoinColumn(name = "order_id", unique = true)
    private CustomerOrder order;
    @Column(nullable = false) private String method;   // COD | CARD | UPI
    @Column(nullable = false) private String status;   // PAID | PENDING
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal amount;
    @Column(unique = true) private String transactionRef;
    private LocalDateTime paidAt;
}
