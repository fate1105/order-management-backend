package com.fer.ordermanagement.order.controller;

import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody OrderRequest req
    ){
        OrderResponse orderResponse = orderService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{id}")
    public OrderResponse getById(
            @PathVariable Long id
    ){
        return orderService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id
    ){
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
