package com.loka.lokabackend.order.controller;

import com.loka.lokabackend.order.dto.OrderDTOs.*;
import com.loka.lokabackend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:8080" })
public class OrderController {

    private final OrderService orderService;

    // ── Orders ────────────────────────────────────────

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody PlaceOrderRequest request) {
        try {
            OrderResponse response = orderService.placeOrder(Long.parseLong(userId), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.getUserOrders(Long.parseLong(userId)));
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        return orderService.getOrderById(id, Long.parseLong(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Cart ──────────────────────────────────────────

    @GetMapping("/api/cart")
    public ResponseEntity<List<CartItemResponse>> getCart(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.getCart(Long.parseLong(userId)));
    }

    @PostMapping("/api/cart")
    public ResponseEntity<CartItemResponse> addToCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody AddToCartRequest request) {
        try {
            CartItemResponse response = orderService.addToCart(Long.parseLong(userId), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/cart/{productId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Long productId,
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateCartRequest request) {
        try {
            CartItemResponse response = orderService.updateCartItem(
                    Long.parseLong(userId), productId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/cart/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long productId,
            @RequestHeader("X-User-Id") String userId) {
        orderService.removeFromCart(Long.parseLong(userId), productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/cart")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("X-User-Id") String userId) {
        orderService.clearCart(Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/orders/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Service is running");
    }
}