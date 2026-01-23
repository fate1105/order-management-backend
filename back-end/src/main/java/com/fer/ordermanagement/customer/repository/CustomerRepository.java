package com.fer.ordermanagement.customer.repository;

import com.fer.ordermanagement.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
