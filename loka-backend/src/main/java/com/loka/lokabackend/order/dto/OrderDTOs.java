package com.loka.lokabackend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceOrderRequest {
        private List<OrderItemRequest> items;
        private BigDecimal total;
        private Map<String, String> shippingAddress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long productId;
        private String name;
        private String image;
        private Integer quantity;
        private BigDecimal price;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderResponse {
        private Long id;
        private String orderNumber;
        private String status;
        private BigDecimal total;
        private List<OrderItemResponse> items;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long productId;
        private String name;
        private String image;
        private Integer quantity;
        private BigDecimal price;
    }

    // ── Cart DTOs ─────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddToCartRequest {
        private Long productId;
        private String name;
        private String image;
        private BigDecimal price;
        private Integer quantity;
        private String category;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCartRequest {
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String name;
        private String image;
        private BigDecimal price;
        private Integer quantity;
        private String category;
    }
}