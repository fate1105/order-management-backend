package com.fer.ordermanagement.inventory.service;

import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.inventory.entity.Inventory;
import com.fer.ordermanagement.inventory.repository.InventoryRepository;
import com.fer.ordermanagement.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Product mockProduct;
    private Inventory mockInventory;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(10L);
        mockProduct.setSku("FER-TS-001");
        mockProduct.setName("FER Classic T-Shirt");
        mockProduct.setPrice(BigDecimal.valueOf(199000));

        mockInventory = Inventory.create(mockProduct);
    }

    //CREATE FOR PRODUCT

    @Test
    @DisplayName("CreateForProduct: Nên tạo inventory mới khi chưa tồn tại")
    void createForProduct_ShouldSaveInventory_WhenNotExists() {
        when(inventoryRepository.existsByProductId(10L)).thenReturn(false);

        inventoryService.createForProduct(mockProduct);

        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("CreateForProduct: Nên bỏ qua khi inventory đã tồn tại")
    void createForProduct_ShouldSkip_WhenAlreadyExists() {
        when(inventoryRepository.existsByProductId(10L)).thenReturn(true);

        inventoryService.createForProduct(mockProduct);

        // Đã tồn tại thì không save thêm
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    //INCREASE

    @Test
    @DisplayName("Increase: Nên ném NotFoundException khi không tìm thấy inventory")
    void increase_ShouldThrowNotFoundException_WhenInventoryNotFound() {
        when(inventoryRepository.findByProductIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> inventoryService.increase(99L, 5));
    }

    @Test
    @DisplayName("Increase: Nên tăng số lượng khi inventory tồn tại")
    void increase_ShouldCallIncreaseOnInventory_WhenFound() {
        Inventory spyInventory = spy(mockInventory);
        when(inventoryRepository.findByProductIdForUpdate(10L)).thenReturn(Optional.of(spyInventory));

        inventoryService.increase(10L, 5);

        verify(spyInventory).increase(5);
    }

    //RESERVE

    @Test
    @DisplayName("Reserve: Nên ném NotFoundException khi không tìm thấy inventory")
    void reserve_ShouldThrowNotFoundException_WhenInventoryNotFound() {
        when(inventoryRepository.findByProductIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> inventoryService.reserve(99L, 2));
    }

    @Test
    @DisplayName("Reserve: Nên gọi reserve trên inventory khi tìm thấy")
    void reserve_ShouldCallReserveOnInventory_WhenFound() {
        mockInventory.increase(10);
        Inventory spyInventory = spy(mockInventory);
        when(inventoryRepository.findByProductIdForUpdate(10L)).thenReturn(Optional.of(spyInventory));

        inventoryService.reserve(10L, 2);

        verify(spyInventory).reserve(2);
    }

    //RELEASE

    @Test
    @DisplayName("Release: Nên ném NotFoundException khi không tìm thấy inventory")
    void release_ShouldThrowNotFoundException_WhenInventoryNotFound() {
        when(inventoryRepository.findByProductIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> inventoryService.release(99L, 2));
    }

    @Test
    @DisplayName("Release: Nên gọi release trên inventory khi tìm thấy")
    void release_ShouldCallReleaseOnInventory_WhenFound() {
        mockInventory.increase(10);
        mockInventory.reserve(2);
        Inventory spyInventory = spy(mockInventory);
        when(inventoryRepository.findByProductIdForUpdate(10L)).thenReturn(Optional.of(spyInventory));

        inventoryService.release(10L, 2);

        verify(spyInventory).release(2);
    }
}