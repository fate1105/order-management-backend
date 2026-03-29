package com.fer.ordermanagement.order.controller;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.enums.OrderStatus;
import com.fer.ordermanagement.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> create(
            @Valid @RequestBody OrderRequest req
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(orderService.create(req))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponse>> getById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                BaseResponse.success(orderService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<OrderResponse>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status
    ) {

        Page<OrderResponse> result = orderService.getAllPaged(page, size, keyword, status);
        return ResponseEntity.ok(
                BaseResponse.success(new PageResponse<>(result))
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse<Void>> cancel(
            @PathVariable Long id
    ){
        orderService.cancel(id);
        return ResponseEntity.ok(
                BaseResponse.success("Hủy đơn hàng thành công")
        );
    }
}
