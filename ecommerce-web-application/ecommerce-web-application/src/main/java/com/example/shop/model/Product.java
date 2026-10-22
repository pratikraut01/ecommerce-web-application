package com.example.shop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "products", indexes = @Index(name = "idx_product_name", columnList = "name"))
@Getter @Setter @NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(length = 1000) private String description;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(nullable = false) private int stock;
    private String imageUrl;
    private boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    private LocalDateTime createdAt = LocalDateTime.now();

    /** Image to display; falls back to a placeholder. */
    public String getImage() {
        return (imageUrl != null && !imageUrl.isBlank()) ? imageUrl : "https://picsum.photos/seed/p" + id + "/400/300";
    }
}
