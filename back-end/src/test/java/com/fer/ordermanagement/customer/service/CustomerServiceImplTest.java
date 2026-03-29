package com.fer.ordermanagement.customer.service;

import com.fer.ordermanagement.common.exception.ConflictException;
import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.customer.repository.CustomerRepository;
import com.fer.ordermanagement.order.repository.OrderRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private OrderRepository orderRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer mockCustomer;
    private CustomerRequest customerRequest;

    @BeforeEach
    void setUp() {
        mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFullName("Nguyen Van A");
        mockCustomer.setPhone("0901234567");
        mockCustomer.setEmail("nguyenvana@gmail.com");
        mockCustomer.setAddress("123 Le Loi, HCM");

        customerRequest = CustomerRequest.builder()
                .fullName("Nguyen Van A")
                .phone("0901234567")
                .email("nguyenvana@gmail.com")
                .address("123 Le Loi, HCM")
                .build();
    }

    //CREATE

    @Test
    @DisplayName("Create: Nên ném ConflictException khi phone đã tồn tại")
    void create_ShouldThrowConflictException_WhenPhoneExists() {
        when(customerRepository.existsByPhone("0901234567")).thenReturn(true);

        assertThrows(ConflictException.class, () -> customerService.create(customerRequest));
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Nên ném ConflictException khi email đã tồn tại")
    void create_ShouldThrowConflictException_WhenEmailExists() {
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail("nguyenvana@gmail.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> customerService.create(customerRequest));
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Nên lưu customer khi dữ liệu hợp lệ")
    void create_ShouldSaveCustomer_WhenValid() {
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        CustomerResponse response = customerService.create(customerRequest);

        assertNotNull(response);
        assertEquals("Nguyen Van A", response.getFullName());
        verify(customerRepository).save(any(Customer.class));
    }

    //UPDATE

    @Test
    @DisplayName("Update: Nên ném NotFoundException khi ID không tồn tại")
    void update_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.update(99L, customerRequest));
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update: Nên ném ConflictException khi phone thuộc về customer khác")
    void update_ShouldThrowConflictException_WhenPhoneBelongsToOther() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.existsByPhoneAndIdNot("0901234567", 1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> customerService.update(1L, customerRequest));
    }

    @Test
    @DisplayName("Update: Nên cập nhật customer khi dữ liệu hợp lệ")
    void update_ShouldReturnUpdatedResponse_WhenValid() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.existsByPhoneAndIdNot(anyString(), eq(1L))).thenReturn(false);
        when(customerRepository.existsByEmailAndIdNot(anyString(), eq(1L))).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        CustomerResponse response = customerService.update(1L, customerRequest);

        assertNotNull(response);
        verify(customerRepository).save(any(Customer.class));
    }

    //GET BY ID

    @Test
    @DisplayName("GetById: Nên ném NotFoundException khi không tìm thấy")
    void getById_ShouldThrowNotFoundException_WhenNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getById(99L));
    }

    @Test
    @DisplayName("GetById: Nên trả về response khi tìm thấy")
    void getById_ShouldReturnResponse_WhenFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        CustomerResponse response = customerService.getById(1L);

        assertNotNull(response);
        assertEquals("Nguyen Van A", response.getFullName());
    }

    //DELETE

    @Test
    @DisplayName("Delete: Nên ném NotFoundException khi ID không tồn tại")
    void delete_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService.delete(99L));
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete: Nên ném ConflictException khi customer còn order")
    void delete_ShouldThrowConflictException_WhenCustomerHasOrders() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.existsByCustomerId(1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> customerService.delete(1L));
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete: Nên xóa customer khi không có order")
    void delete_ShouldDeleteCustomer_WhenNoOrders() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.existsByCustomerId(1L)).thenReturn(false);

        customerService.delete(1L);

        verify(customerRepository).deleteById(1L);
    }

    //GET ALL PAGED

    @Test
    @DisplayName("GetAllPaged: Nên trả về đúng trang customer")
    void getAllPaged_ShouldReturnPageOfCustomers() {
        Page<Customer> page = new PageImpl<>(List.of(mockCustomer));
        when(customerRepository.searchWithPaging(eq("Nguyen"), any(Pageable.class)))
                .thenReturn(page);

        Page<CustomerResponse> result = customerService.getAllPaged(0, 10, "Nguyen");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(customerRepository).searchWithPaging(eq("Nguyen"), any(Pageable.class));
    }

    @Test
    @DisplayName("GetAllPaged: Nên truyền null keyword khi keyword là blank")
    void getAllPaged_ShouldPassNullKeyword_WhenKeywordIsBlank() {
        Page<Customer> page = new PageImpl<>(Collections.emptyList());
        when(customerRepository.searchWithPaging(isNull(), any(Pageable.class)))
                .thenReturn(page);

        customerService.getAllPaged(0, 10, "   ");

        verify(customerRepository).searchWithPaging(isNull(), any(Pageable.class));
    }
}