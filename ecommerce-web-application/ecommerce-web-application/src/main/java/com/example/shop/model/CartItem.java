package com.example.shop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
@Getter @Setter @NoArgsConstructor
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(optional = false) @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false) private int quantity;

    public BigDecimal getSubtotal() { return product.getPrice().multiply(BigDecimal.valueOf(quantity)); }
}
