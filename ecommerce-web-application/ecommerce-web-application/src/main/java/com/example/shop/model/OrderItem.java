package com.example.shop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name = "order_items")
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "order_id")
    private CustomerOrder order;
    @ManyToOne(optional = false) @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false) private int quantity;
    /** Price snapshot at purchase time. */
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal unitPrice;

    public BigDecimal getSubtotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
