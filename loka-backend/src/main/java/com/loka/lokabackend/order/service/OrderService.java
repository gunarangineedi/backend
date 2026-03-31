package com.loka.lokabackend.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loka.lokabackend.order.dto.OrderDTOs.*;
import com.loka.lokabackend.order.model.CartItem;
import com.loka.lokabackend.order.model.Order;
import com.loka.lokabackend.order.model.OrderItem;
import com.loka.lokabackend.order.repository.CartRepository;
import com.loka.lokabackend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    // ── Orders ────────────────────────────────────────

    @Transactional
    public OrderResponse placeOrder(Long userId, PlaceOrderRequest request) {
        String orderNumber = generateOrderNumber();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(userId)
                .total(request.getTotal() != null ? request.getTotal() : BigDecimal.ZERO)
                .status("Confirmed")
                .shippingAddress(serializeAddress(request.getShippingAddress()))
                .build();

        // Map items
        if (request.getItems() != null) {
            List<OrderItem> items = request.getItems().stream().map(req -> {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(req.getProductId());
                item.setProductName(req.getName() != null ? req.getName() : "Product");
                item.setProductImage(req.getImage());
                item.setQuantity(req.getQuantity() != null ? req.getQuantity() : 1);
                item.setPrice(req.getPrice() != null ? req.getPrice() : BigDecimal.ZERO);
                return item;
            }).collect(Collectors.toList());
            order.setItems(items);

            // Recalculate total if not provided
            if (request.getTotal() == null) {
                BigDecimal total = items.stream()
                        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                order.setTotal(total);
            }
        }

        Order saved = orderRepository.save(order);

        // Clear cart after successful order
        try {
            cartRepository.deleteByUserId(userId);
        } catch (Exception e) {
            log.warn("Could not clear cart for user {}: {}", userId, e.getMessage());
        }

        return toOrderResponse(saved);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toOrderResponse).collect(Collectors.toList());
    }

    public Optional<OrderResponse> getOrderById(Long orderId, Long userId) {
        return orderRepository.findById(orderId)
                .filter(o -> o.getUserId().equals(userId))
                .map(this::toOrderResponse);
    }

    // ── Cart ──────────────────────────────────────────

    public List<CartItemResponse> getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .stream().map(this::toCartItemResponse).collect(Collectors.toList());
    }

    @Transactional
    public CartItemResponse addToCart(Long userId, AddToCartRequest request) {
        Optional<CartItem> existing = cartRepository.findByUserIdAndProductId(userId, request.getProductId());

        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(item.getQuantity() + (request.getQuantity() != null ? request.getQuantity() : 1));
        } else {
            item = CartItem.builder()
                    .userId(userId)
                    .productId(request.getProductId())
                    .productName(request.getName() != null ? request.getName() : "Product")
                    .productImage(request.getImage())
                    .price(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO)
                    .quantity(request.getQuantity() != null ? request.getQuantity() : 1)
                    .category(request.getCategory())
                    .build();
        }
        return toCartItemResponse(cartRepository.save(item));
    }

    @Transactional
    public CartItemResponse updateCartItem(Long userId, Long productId, UpdateCartRequest request) {
        CartItem item = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(request.getQuantity());
        return toCartItemResponse(cartRepository.save(item));
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    // ── Helpers ───────────────────────────────────────

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((int) (Math.random() * 9000) + 1000);
        return "ORD-" + timestamp.substring(6) + random;
    }

    private String serializeAddress(Object address) {
        if (address == null)
            return null;
        try {
            return objectMapper.writeValueAsString(address);
        } catch (Exception e) {
            return address.toString();
        }
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems() == null ? List.of()
                : order.getItems().stream().map(item -> OrderItemResponse.builder()
                        .productId(item.getProductId())
                        .name(item.getProductName())
                        .image(item.getProductImage())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build()).collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .total(order.getTotal())
                .items(items)
                .createdAt(order.getCreatedAt())
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .name(item.getProductName())
                .image(item.getProductImage())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .category(item.getCategory())
                .build();
    }
}