package com.fer.ordermanagement.order.service;

import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);

    OrderResponse getById(Long id);

    List<OrderResponse> getAll();

    void cancel(Long id);
}
