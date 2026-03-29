package com.fer.ordermanagement.order.controller;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import com.fer.ordermanagement.order.controller.api.OrderApi;
import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.enums.OrderStatus;
import com.fer.ordermanagement.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<BaseResponse<OrderResponse>> create(OrderRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(orderService.create(req))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<OrderResponse>> getById(Long id) {
        return ResponseEntity.ok(
                BaseResponse.success(orderService.getById(id))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponse<OrderResponse>>> getAllPaged(
            int page, int size, String keyword, OrderStatus status) {
        Page<OrderResponse> result = orderService.getAllPaged(page, size, keyword, status);
        return ResponseEntity.ok(
                BaseResponse.success(new PageResponse<>(result))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> cancel(Long id) {
        orderService.cancel(id);
        return ResponseEntity.ok(
                BaseResponse.success("Hủy đơn hàng thành công")
        );
    }
}