package com.fer.ordermanagement.order.service;

import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.enums.OrderStatus;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.enums.ProductStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);

    OrderResponse getById(Long id);

    void cancel(Long id);

    Page<OrderResponse> getAllPaged(int page, int size, String keyword, OrderStatus status);
}
