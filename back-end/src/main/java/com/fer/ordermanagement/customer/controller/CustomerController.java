package com.fer.ordermanagement.customer.controller;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.service.CustomerService;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponse<CustomerResponse>> create(
            @Valid @RequestBody CustomerRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(customerService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request
    ){
        return ResponseEntity.ok(
                BaseResponse.success(customerService.update(id, request))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CustomerResponse>> getById(
            @PathVariable("id") Long customerId
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(customerService.getById(customerId))
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<CustomerResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword

    ) {
        return ResponseEntity.ok(
                BaseResponse.success(customerService.getAllPaged(page, size, keyword))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable("id") Long customerId
    ) {
        customerService.delete(customerId);
        return ResponseEntity.ok(
            BaseResponse.success("Xóa khách hàng thành công")
        );
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getOrderHistory(
            @PathVariable("id") Long customerId
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(orderService.getOrdersByCustomerId(customerId))
        );
    }
}
