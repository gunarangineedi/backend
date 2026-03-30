package com.loka.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private String imageUrl;
    private Integer stockQuantity;
    private Boolean featured;
    private String material;
    private String careInstructions;
    private List<String> sizes;
    private LocalDateTime createdAt;
    private Integer discount;
}
