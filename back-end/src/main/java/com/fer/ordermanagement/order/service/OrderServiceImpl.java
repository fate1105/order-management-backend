package com.fer.ordermanagement.order.service;

import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.customer.repository.CustomerRepository;
import com.fer.ordermanagement.inventory.service.InventoryService;
import com.fer.ordermanagement.order.dto.OrderItemRequest;
import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.entity.OrderItem;
import com.fer.ordermanagement.order.mapper.OrderMapper;
import com.fer.ordermanagement.order.repository.OrderRepository;
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryService inventoryService;

    public OrderResponse create(OrderRequest req){
        // 1. validate customer
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + req.getCustomerId()));

        // 2. create order (chưa save)
        String orderCode = generateOrderCode();
        Order order = Order.create(orderCode, customer);

        for (OrderItemRequest item : req.getItems()){
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            // 3.1 reserve inventory
            inventoryService.reserve(product.getId(), item.getQuantity());

            // 3.2 create order item
            OrderItem orderItem = OrderItem.create(order, product, item.getQuantity());

            // 3.3 add vào order
            order.addItem(orderItem);
        }

        // 4. save order (cascade save order_items)
        Order saved = orderRepository.save(order);

        // 5. return dto
        return OrderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getById(Long orderId){
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAll(){
        return orderRepository.findAllWithItems()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    public void cancel(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        order.getItems().forEach(orderItem ->
                inventoryService.release(
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity()
                )
        );

        order.cancel();
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
