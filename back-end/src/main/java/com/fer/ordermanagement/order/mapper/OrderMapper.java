package com.fer.ordermanagement.order.mapper;

import com.fer.ordermanagement.order.dto.OrderItemResponse;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {
    public static OrderResponse toResponse(Order order){
        if(order == null) return null;

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(toItemResponses(order.getItems()))
                .build();
    }

    private static List<OrderItemResponse> toItemResponses(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return List.of();

        return items.stream()
                .map(OrderMapper::toItemResponse)
                .toList();
    }

    private static OrderItemResponse toItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productSku(item.getProduct().getSku())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
}
