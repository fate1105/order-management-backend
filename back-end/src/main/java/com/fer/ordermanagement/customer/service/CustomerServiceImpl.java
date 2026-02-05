package com.fer.ordermanagement.customer.service;

import com.fer.ordermanagement.common.exception.ConflictException;
import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.customer.dto.CustomerOrderResponse;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.customer.mapper.CustomerMapper;
import com.fer.ordermanagement.customer.repository.CustomerRepository;
import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public CustomerResponse create(CustomerRequest req){
        if (req.getPhone() != null && customerRepository.existsByPhone(req.getPhone()))
            throw new ConflictException("Phone already exists: " + req.getPhone());

        if (req.getEmail() != null && customerRepository.existsByEmail(req.getEmail())) {
            throw new ConflictException("Email already exists: " + req.getEmail());
        }

        Customer customer = new Customer();
        customer.setFullName(req.getFullName());
        customer.setPhone(req.getPhone());
        customer.setEmail(req.getEmail());
        customer.setAddress(req.getAddress());

        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toResponse(saved);
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest req) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        if (req.getPhone() != null &&
                customerRepository.existsByPhoneAndIdNot(req.getPhone(), id)) {
            throw new ConflictException("Phone already exists: " + req.getPhone());
        }

        if (req.getEmail() != null &&
                customerRepository.existsByEmailAndIdNot(req.getEmail(), id)) {
            throw new ConflictException("Email already exists: " + req.getEmail());
        }

        customer.setFullName(req.getFullName());
        customer.setPhone(req.getPhone());
        customer.setEmail(req.getEmail());
        customer.setAddress(req.getAddress());

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse getById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAll() {

        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer not found: " + id);
        }

        boolean hasOrder = orderRepository.existsByCustomerId(id);
        if (hasOrder) {
            throw new ConflictException("Cannot delete customer who has orders");
        }

        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerOrderResponse> getOrderHistory(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer not found: " + customerId);
        }

        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        return orders.stream()
                .map(CustomerMapper::toOrderResponse)
                .toList();
    }
}
