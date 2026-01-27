package com.fer.ordermanagement.customer.mapper;

import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.entity.Customer;

public class CustomerMapper {
    public static CustomerResponse toResponse(Customer customer){
        if (customer == null) return null;

        return CustomerResponse.builder()
                .customerId(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }
}
