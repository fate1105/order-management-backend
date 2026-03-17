package com.fer.ordermanagement.customer.service;

import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.order.dto.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest req);
    CustomerResponse update(Long id, CustomerRequest req);
    CustomerResponse getById(Long id);
    Customer getCustomerEntityById(Long customerId);
    void delete(Long id);

    Page<CustomerResponse> getAllPaged(int page, int size, String keyword);
}
