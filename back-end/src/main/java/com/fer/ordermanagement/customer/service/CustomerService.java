package com.fer.ordermanagement.customer.service;

import com.fer.ordermanagement.customer.dto.CustomerOrderResponse;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest req);
    CustomerResponse update(Long id, CustomerRequest req);
    CustomerResponse getById(Long id);
    void delete(Long id);

    List<CustomerOrderResponse> getOrderHistory(Long customerId);
    Page<CustomerResponse> getAllPaged(int page, int size, String keyword);
}
