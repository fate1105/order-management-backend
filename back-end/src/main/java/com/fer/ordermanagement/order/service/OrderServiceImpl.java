package com.fer.ordermanagement.order.service;

import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.customer.repository.CustomerRepository;
import com.fer.ordermanagement.inventory.service.InventoryService;
import com.fer.ordermanagement.order.dto.OrderItemRequest;
import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.entity.OrderItem;
import com.fer.ordermanagement.order.enums.OrderStatus;
import com.fer.ordermanagement.order.mapper.OrderMapper;
import com.fer.ordermanagement.order.repository.OrderRepository;
import com.fer.ordermanagement.payment.service.PaymentService;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.enums.ProductStatus;
import com.fer.ordermanagement.product.mapper.ProductMapper;
import com.fer.ordermanagement.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final PaymentService paymentService;

    public OrderResponse create(OrderRequest req){
        // 1. validate customer
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found: " + req.getCustomerId()));

        // 2. create order (chưa save)
        String orderCode = generateOrderCode();
        Order order = Order.create(orderCode, customer);

        for (OrderItemRequest item : req.getItems()){
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + item.getProductId()));

            // 3.1 reserve inventory
            inventoryService.reserve(product.getId(), item.getQuantity());

            // 3.2 create order item
            OrderItem orderItem = OrderItem.create(order, product, item.getQuantity());

            // 3.3 add vào order
            order.addItem(orderItem);
        }

        // 4. save order (cascade save order_items)
        Order saved = orderRepository.save(order);
        paymentService.createForOrder(saved);
        // 5. return dto
        return OrderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getById(Long orderId){
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        return OrderMapper.toResponse(order);
    }

    @Override
    public void cancel(Long orderId){
        paymentService.markFailed(orderId);
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public Page<OrderResponse> getAllPaged(
            int page,
            int size,
            String keyword,
            OrderStatus status
    ) {
        // Validate page và size
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        // Chuẩn hóa keyword
        String normalizedKeyword = (keyword == null || keyword.isBlank())
                ? null
                : keyword.trim();

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by("createdAt").descending()
        );

        Page<Order> orderPage =
                orderRepository.searchWithPaging(normalizedKeyword, status, pageable);

        return orderPage.map(OrderMapper::toResponse);
    }
}
