package com.fer.ordermanagement.customer.service;

import com.fer.ordermanagement.customer.dto.CustomerOrderResponse;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest req);
    CustomerResponse update(Long id, CustomerRequest req);
    CustomerResponse getById(Long id);
    List<CustomerResponse> getAll();
    void delete(Long id);

    List<CustomerOrderResponse> getOrderHistory(Long customerId);
}
