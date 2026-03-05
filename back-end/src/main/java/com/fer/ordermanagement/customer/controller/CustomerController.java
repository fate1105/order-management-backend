package com.fer.ordermanagement.customer.controller;

import com.fer.ordermanagement.common.response.ApiResponse;
import com.fer.ordermanagement.customer.dto.CustomerOrderResponse;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.service.CustomerService;
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

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> create(
            @Valid @RequestBody CustomerRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.created(customerService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request
    ){
        return ResponseEntity.ok(
                ApiResponse.success(customerService.update(id, request))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(
            @PathVariable("id") Long customerId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(customerService.getById(customerId))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword

    ) {
        return ResponseEntity.ok(
                ApiResponse.success(customerService.getAllPaged(page, size, keyword))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("id") Long customerId
    ) {
        customerService.delete(customerId);
        return ResponseEntity.ok(
            ApiResponse.success("Xóa khách hàng thành công")
        );
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<ApiResponse<List<CustomerOrderResponse>>> getOrderHistory(
            @PathVariable("id") Long customerId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(customerService.getOrderHistory(customerId))
        );
    }
}
