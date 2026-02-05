package com.fer.ordermanagement.customer.mapper;

import com.fer.ordermanagement.customer.dto.CustomerOrderResponse;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.order.entity.Order;

public final class CustomerMapper {
    private CustomerMapper() {}

    public static CustomerResponse toResponse(Customer customer){
        if (customer == null) return null;

        return CustomerResponse.builder()
                .customerId(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }

    public static CustomerOrderResponse toOrderResponse(Order order){
        if (order == null) return null;

        return CustomerOrderResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
