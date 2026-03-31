package com.loka.lokabackend.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false)
    private String category;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "stock_quantity")
    @Builder.Default
    private Integer stockQuantity = 100;

    @Column(name = "featured")
    @Builder.Default
    private Boolean featured = false;

    @Column(name = "active")
    @Builder.Default
    private Boolean active = true;

    private String material;

    @Column(name = "care_instructions")
    private String careInstructions;

    // Comma-separated sizes: "XS,S,M,L,XL"
    private String sizes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}