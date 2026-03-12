package com.fer.ordermanagement.order.service;

import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.customer.service.CustomerService;
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
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // ✅ Đã thay thế Repository bằng Service của các module khác
    private final ProductService productService;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    @Override
    public OrderResponse create(OrderRequest req) {
        Customer customer = customerService.getCustomerEntityById(req.getCustomerId());

        List<Long> productIds = req.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .toList();

        Map<Long, Product> productMap = productService.getProductsByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        String orderCode = generateOrderCode();
        Order order = Order.create(orderCode, customer);

        for (OrderItemRequest itemReq : req.getItems()) {
            Product product = productMap.get(itemReq.getProductId());
            if (product == null) {
                throw new NotFoundException("Product not found: " + itemReq.getProductId());
            }

            inventoryService.reserve(product.getId(), itemReq.getQuantity());

            OrderItem orderItem = OrderItem.create(order, product, itemReq.getQuantity());
            order.addItem(orderItem);
        }

        Order saved = orderRepository.save(order);
        paymentService.createForOrder(saved);

        return OrderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getById(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        return OrderMapper.toResponse(order);
    }

    @Override
    public void cancel(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        order.setStatus(OrderStatus.CANCELLED);

        for(OrderItem item : order.getItems()) {
            inventoryService.restore(item.getProduct().getId(), item.getQuantity());
        }

        paymentService.markFailed(orderId);

        orderRepository.save(order);
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
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

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