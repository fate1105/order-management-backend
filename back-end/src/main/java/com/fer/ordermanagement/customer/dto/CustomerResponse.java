package com.fer.ordermanagement.customer.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerResponse {
    Long customerId;

    String fullName;

    String phone;

    String email;

    String address;
}
