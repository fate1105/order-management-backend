package com.fer.ordermanagement.order.service;

import com.fer.ordermanagement.audit.service.AuditLogService;
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
import com.fer.ordermanagement.order.repository.OrderRepository;
import com.fer.ordermanagement.payment.service.PaymentService;
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductService productService;
    @Mock private CustomerService customerService;
    @Mock private InventoryService inventoryService;
    @Mock private PaymentService paymentService;
    @Mock private AuditLogService auditLogService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer mockCustomer;
    private Product mockProduct;
    private Order mockOrder;
    private OrderItem mockOrderItem;
    private OrderRequest orderRequest;
    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        // Customer
        mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFullName("Nguyen Van A");

        // Product
        mockProduct = new Product();
        mockProduct.setId(10L);
        mockProduct.setSku("FER-TS-001");
        mockProduct.setName("FER Classic T-Shirt");
        mockProduct.setPrice(BigDecimal.valueOf(199000));

        // OrderItem
        mockOrderItem = OrderItem.create(null, mockProduct, 2);

        // Order
        mockOrder = Order.create("ORD-ABCD1234", mockCustomer);
        mockOrder.addItem(mockOrderItem);

        // Request
        orderItemRequest = OrderItemRequest.builder()
                .productId(10L)
                .quantity(2)
                .build();

        orderRequest = OrderRequest.builder()
                .customerId(1L)
                .items(List.of(orderItemRequest))
                .build();
    }

    //CREATE

    @Test
    @DisplayName("Create: Nên ném NotFoundException khi product không tồn tại trong map")
    void create_ShouldThrowNotFoundException_WhenProductNotFound() {
        // Given
        when(customerService.getCustomerEntityById(1L)).thenReturn(mockCustomer);
        // productService trả về list rỗng → productMap sẽ không có productId = 10
        when(productService.getProductsByIds(List.of(10L))).thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(NotFoundException.class, () -> orderService.create(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
        verify(paymentService, never()).createForOrder(any(Order.class));
    }

    @Test
    @DisplayName("Create: Nên tạo order, reserve inventory và tạo payment khi hợp lệ")
    void create_ShouldCreateOrderAndReserveInventory_WhenValid() {
        // Given
        when(customerService.getCustomerEntityById(1L)).thenReturn(mockCustomer);
        when(productService.getProductsByIds(List.of(10L))).thenReturn(List.of(mockProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // When
        OrderResponse response = orderService.create(orderRequest);

        // Then
        assertNotNull(response);
        // Reserve inventory phải được gọi với đúng productId và quantity
        verify(inventoryService).reserve(eq(10L), eq(2));
        // Save và createForOrder phải được gọi
        verify(orderRepository).save(any(Order.class));
        verify(paymentService).createForOrder(any(Order.class));
    }

    //GET BY ID

    @Test
    @DisplayName("GetById: Nên ném NotFoundException khi orderId không tồn tại")
    void getById_ShouldThrowNotFoundException_WhenOrderNotFound() {
        when(orderRepository.findByIdWithItems(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.getById(99L));
    }

    @Test
    @DisplayName("GetById: Nên trả về OrderResponse khi tìm thấy")
    void getById_ShouldReturnResponse_WhenOrderFound() {
        when(orderRepository.findByIdWithItems(100L)).thenReturn(Optional.of(mockOrder));

        OrderResponse response = orderService.getById(100L);

        assertNotNull(response);
        assertEquals("ORD-ABCD1234", response.getOrderCode());
    }

    //CANCEL

    @Test
    @DisplayName("Cancel: Nên ném NotFoundException khi orderId không tồn tại")
    void cancel_ShouldThrowNotFoundException_WhenOrderNotFound() {
        when(orderRepository.findByIdWithItems(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.cancel(99L));
        verify(inventoryService, never()).release(anyLong(), anyInt());
        verify(paymentService, never()).markFailed(anyLong());
    }

    @Test
    @DisplayName("Cancel: Nên đổi status, release inventory và markFailed payment")
    void cancel_ShouldReleaseInventoryAndMarkPaymentFailed_WhenOrderFound() {
        when(orderRepository.findByIdWithItems(100L)).thenReturn(Optional.of(mockOrder));

        orderService.cancel(100L);

        // Status phải đổi thành CANCELLED
        assertEquals(OrderStatus.CANCELLED, mockOrder.getStatus());
        // Release inventory cho từng item
        verify(inventoryService).release(eq(10L), eq(2));
        // Payment phải bị đánh dấu thất bại
        verify(paymentService).markFailed(eq(100L));
        // Phải save lại order
        verify(orderRepository).save(mockOrder);
    }

    //GET ORDERS BY CUSTOMER ID

    @Test
    @DisplayName("GetOrdersByCustomerId: Nên ném NotFoundException khi customer không tồn tại")
    void getOrdersByCustomerId_ShouldThrowNotFoundException_WhenCustomerNotFound() {
        when(customerService.getCustomerEntityById(99L))
                .thenThrow(new NotFoundException("Customer not found: 99"));

        assertThrows(NotFoundException.class, () -> orderService.getOrdersByCustomerId(99L));
        verify(orderRepository, never()).findByCustomerIdOrderByCreatedAtDesc(anyLong());
    }

    @Test
    @DisplayName("GetOrdersByCustomerId: Nên trả về danh sách order khi customer tồn tại")
    void getOrdersByCustomerId_ShouldReturnOrders_WhenCustomerExists() {
        when(customerService.getCustomerEntityById(1L)).thenReturn(mockCustomer);
        when(orderRepository.findByCustomerIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(mockOrder));

        List<OrderResponse> result = orderService.getOrdersByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ORD-ABCD1234", result.get(0).getOrderCode());
    }

    @Test
    @DisplayName("GetOrdersByCustomerId: Nên trả về list rỗng khi customer chưa có order nào")
    void getOrdersByCustomerId_ShouldReturnEmptyList_WhenNoOrders() {
        when(customerService.getCustomerEntityById(1L)).thenReturn(mockCustomer);
        when(orderRepository.findByCustomerIdOrderByCreatedAtDesc(1L))
                .thenReturn(Collections.emptyList());

        List<OrderResponse> result = orderService.getOrdersByCustomerId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //GET ALL PAGED

    @Test
    @DisplayName("GetAllPaged: Nên trả về đúng trang order theo keyword và status")
    void getAllPaged_ShouldReturnPageOfOrders() {
        Page<Order> orderPage = new PageImpl<>(List.of(mockOrder));
        when(orderRepository.searchWithPaging(any(), any(), any(Pageable.class)))
                .thenReturn(orderPage);

        Page<OrderResponse> result = orderService.getAllPaged(0, 10, "ORD", OrderStatus.CREATED);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(orderRepository).searchWithPaging(eq("ORD"), eq(OrderStatus.CREATED), any(Pageable.class));
    }

    @Test
    @DisplayName("GetAllPaged: Nên truyền null keyword khi keyword là blank")
    void getAllPaged_ShouldPassNullKeyword_WhenKeywordIsBlank() {
        Page<Order> orderPage = new PageImpl<>(List.of(mockOrder));
        when(orderRepository.searchWithPaging(isNull(), any(), any(Pageable.class)))
                .thenReturn(orderPage);

        orderService.getAllPaged(0, 10, "   ", OrderStatus.CREATED);

        verify(orderRepository).searchWithPaging(isNull(), eq(OrderStatus.CREATED), any(Pageable.class));
    }
}