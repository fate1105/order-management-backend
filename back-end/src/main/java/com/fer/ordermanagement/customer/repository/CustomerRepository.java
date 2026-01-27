package com.fer.ordermanagement.customer.repository;

import com.fer.ordermanagement.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone, Long customerId);
    boolean existsByEmailAndIdNot(String email, Long customerId);
}
