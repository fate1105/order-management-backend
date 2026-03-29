package com.fer.ordermanagement.customer.controller;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.customer.controller.api.CustomerApi;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.service.OrderService;
import com.fer.ordermanagement.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;
    private final OrderService orderService;

    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> create(CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(customerService.create(request))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> update(Long id, CustomerRequest request) {
        return ResponseEntity.ok(
                BaseResponse.success(customerService.update(id, request))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> getById(Long id) {
        return ResponseEntity.ok(
                BaseResponse.success(customerService.getById(id))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<Page<CustomerResponse>>> getAll(
            int page, int size, String keyword) {
        return ResponseEntity.ok(
                BaseResponse.success(customerService.getAllPaged(page, size, keyword))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(Long id) {
        customerService.delete(id);
        return ResponseEntity.ok(
                BaseResponse.success("Xóa khách hàng thành công")
        );
    }

    @Override
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getOrderHistory(Long id) {
        return ResponseEntity.ok(
                BaseResponse.success(orderService.getOrdersByCustomerId(id))
        );
    }
}