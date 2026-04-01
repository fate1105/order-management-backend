package com.fer.ordermanagement.product.service;

import com.fer.ordermanagement.audit.service.AuditLogService;
import com.fer.ordermanagement.category.entity.Category;
import com.fer.ordermanagement.category.service.CategoryService;
import com.fer.ordermanagement.common.exception.ConflictException;
import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.inventory.service.InventoryService;
import com.fer.ordermanagement.product.dto.ProductCreateRequest;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.dto.ProductUpdateRequest;
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.enums.ProductStatus;
import com.fer.ordermanagement.product.repository.ProductRepository;
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
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryService categoryService;
    @Mock private InventoryService inventoryService;
    @Mock private AuditLogService auditLogService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductCreateRequest createRequest;
    private ProductUpdateRequest updateRequest;
    private Category mockCategory;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        createRequest = ProductCreateRequest.builder()
                .sku("FER-TS-001")
                .name("FER Classic T-Shirt")
                .price(BigDecimal.valueOf(199000))
                .description("Classic cotton T-shirt")
                .categoryId(1L)
                .build();

        updateRequest = ProductUpdateRequest.builder()
                .name("FER Classic T-Shirt Updated")
                .price(BigDecimal.valueOf(219000))
                .description("Updated description")
                .status(ProductStatus.ACTIVE)
                .categoryId(1L)
                .build();

        mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setName("T-Shirt");

        mockProduct = new Product();
        mockProduct.setId(10L);
        mockProduct.setSku("FER-TS-001");
        mockProduct.setName("FER Classic T-Shirt");
        mockProduct.setPrice(BigDecimal.valueOf(199000));
        mockProduct.setCategory(mockCategory);
    }

    //CREATE

    @Test
    @DisplayName("Create: Nên ném ConflictException khi SKU đã tồn tại")
    void create_ShouldThrowConflictException_WhenSkuExists() {
        when(productRepository.existsBySku("FER-TS-001")).thenReturn(true);

        assertThrows(ConflictException.class, () -> productService.create(createRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Create: Nên lưu product và tạo inventory khi dữ liệu hợp lệ")
    void create_ShouldSaveProductAndCreateInventory_WhenValid() {
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(categoryService.findById(1L)).thenReturn(mockCategory);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        ProductResponse response = productService.create(createRequest);

        assertNotNull(response);
        assertEquals("FER-TS-001", response.getSku());
        verify(productRepository).save(any(Product.class));
        verify(inventoryService).createForProduct(any(Product.class));
    }

    //UPDATE

    @Test
    @DisplayName("Update: Nên ném NotFoundException khi ID không tồn tại")
    void update_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(99L, updateRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Update: Nên cập nhật và trả về response khi ID tồn tại")
    void update_ShouldReturnUpdatedResponse_WhenIdExists() {
        // Tạo product đã cập nhật để mock trả về
        Product updatedProduct = new Product();
        updatedProduct.setId(10L);
        updatedProduct.setSku("FER-TS-001");
        updatedProduct.setName("FER Classic T-Shirt Updated");
        updatedProduct.setPrice(BigDecimal.valueOf(219000));
        updatedProduct.setCategory(mockCategory);

        when(productRepository.findById(10L)).thenReturn(Optional.of(mockProduct));
        when(categoryService.findById(1L)).thenReturn(mockCategory);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.update(10L, updateRequest);

        assertNotNull(response);
        assertEquals("FER Classic T-Shirt Updated", response.getName());
        verify(productRepository).save(any(Product.class));
    }

    //GET BY ID

    @Test
    @DisplayName("GetById: Nên ném NotFoundException khi không tìm thấy ID")
    void getById_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(productRepository.findByIdWithCategory(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    @DisplayName("GetById: Nên trả về response khi tìm thấy ID")
    void getById_ShouldReturnResponse_WhenIdExists() {
        when(productRepository.findByIdWithCategory(10L)).thenReturn(Optional.of(mockProduct));

        ProductResponse response = productService.getById(10L);

        assertNotNull(response);
        assertEquals("FER-TS-001", response.getSku());
        assertEquals("FER Classic T-Shirt", response.getName());
    }

    //DELETE

    @Test
    @DisplayName("Delete: Nên gọi deleteById khi ID tồn tại")
    void delete_ShouldCallDeleteById_WhenIdExists() {
        when(productRepository.existsById(10L)).thenReturn(true);

        productService.delete(10L);

        verify(productRepository, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("Delete: Nên ném NotFoundException khi ID không tồn tại")
    void delete_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.delete(99L));
        verify(productRepository, never()).deleteById(anyLong());
    }

    //GET PRODUCTS BY IDS

    @Test
    @DisplayName("GetProductsByIds: Nên trả về emptyList khi list truyền vào null")
    void getProductsByIds_ShouldReturnEmptyList_WhenIdsIsNull() {
        List<Product> result = productService.getProductsByIds(null);

        assertTrue(result.isEmpty());
        verify(productRepository, never()).findAllByIdIn(any());
    }

    @Test
    @DisplayName("GetProductsByIds: Nên trả về emptyList khi list truyền vào rỗng")
    void getProductsByIds_ShouldReturnEmptyList_WhenIdsIsEmpty() {
        List<Product> result = productService.getProductsByIds(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(productRepository, never()).findAllByIdIn(any());
    }

    @Test
    @DisplayName("GetProductsByIds: Nên trả về danh sách product khi IDs hợp lệ")
    void getProductsByIds_ShouldReturnProducts_WhenIdsAreValid() {
        List<Long> ids = List.of(10L);
        when(productRepository.findAllByIdIn(ids)).thenReturn(List.of(mockProduct));

        List<Product> result = productService.getProductsByIds(ids);

        assertEquals(1, result.size());
        assertEquals("FER-TS-001", result.get(0).getSku());
    }

    //GET ALL PAGED

    @Test
    @DisplayName("GetAllPaged: Nên trả về đúng trang sản phẩm theo keyword và status")
    void getAllPaged_ShouldReturnPageOfProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(mockProduct));
        when(productRepository.searchWithPaging(any(), any(), any(Pageable.class)))
                .thenReturn(productPage);

        Page<ProductResponse> result = productService.getAllPaged(0, 10, "FER", ProductStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("FER-TS-001", result.getContent().get(0).getSku());
        verify(productRepository).searchWithPaging(eq("FER"), eq(ProductStatus.ACTIVE), any(Pageable.class));
    }

    @Test
    @DisplayName("GetAllPaged: Nên truyền keyword = null khi keyword trống")
    void getAllPaged_ShouldPassNullKeyword_WhenKeywordIsBlank() {
        Page<Product> productPage = new PageImpl<>(List.of(mockProduct));
        when(productRepository.searchWithPaging(isNull(), any(), any(Pageable.class)))
                .thenReturn(productPage);

        productService.getAllPaged(0, 10, "   ", ProductStatus.ACTIVE);

        // normalizedKeyword phải là null khi keyword chỉ có khoảng trắng
        verify(productRepository).searchWithPaging(isNull(), eq(ProductStatus.ACTIVE), any(Pageable.class));
    }
}