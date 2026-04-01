package com.fer.ordermanagement.category.service;

import com.fer.ordermanagement.audit.service.AuditLogService;
import com.fer.ordermanagement.category.dto.CategoryRequest;
import com.fer.ordermanagement.category.dto.CategoryResponse;
import com.fer.ordermanagement.category.entity.Category;
import com.fer.ordermanagement.category.repository.CategoryRepository;
import com.fer.ordermanagement.common.exception.ConflictException;
import com.fer.ordermanagement.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private AuditLogService auditLogService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category mockCategory;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setName("T-Shirt");
        mockCategory.setDescription("All T-Shirt products");

        categoryRequest = CategoryRequest.builder()
                .name("T-Shirt")
                .description("All T-Shirt products")
                .build();
    }

    //CREATE

    @Test
    @DisplayName("Create: Nên ném ConflictException khi tên category đã tồn tại")
    void create_ShouldThrowConflictException_WhenNameExists() {
        when(categoryRepository.existsByNameIgnoreCase("T-Shirt")).thenReturn(true);

        assertThrows(ConflictException.class, () -> categoryService.create(categoryRequest));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Nên lưu category khi tên hợp lệ")
    void create_ShouldSaveCategory_WhenValid() {
        when(categoryRepository.existsByNameIgnoreCase("T-Shirt")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        CategoryResponse response = categoryService.create(categoryRequest);

        assertNotNull(response);
        assertEquals("T-Shirt", response.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    //UPDATE

    @Test
    @DisplayName("Update: Nên ném NotFoundException khi ID không tồn tại")
    void update_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.update(99L, categoryRequest));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update: Nên ném ConflictException khi tên thuộc về category khác")
    void update_ShouldThrowConflictException_WhenNameBelongsToOther() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(categoryRepository.existsByNameIgnoreCaseAndIdNot("T-Shirt", 1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> categoryService.update(1L, categoryRequest));
    }

    @Test
    @DisplayName("Update: Nên cập nhật category khi dữ liệu hợp lệ")
    void update_ShouldReturnUpdatedResponse_WhenValid() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(categoryRepository.existsByNameIgnoreCaseAndIdNot(anyString(), eq(1L))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        CategoryResponse response = categoryService.update(1L, categoryRequest);

        assertNotNull(response);
        verify(categoryRepository).save(any(Category.class));
    }

    //GET BY ID

    @Test
    @DisplayName("GetById: Nên ném NotFoundException khi không tìm thấy")
    void getById_ShouldThrowNotFoundException_WhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getById(99L));
    }

    @Test
    @DisplayName("GetById: Nên trả về response khi tìm thấy")
    void getById_ShouldReturnResponse_WhenFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        CategoryResponse response = categoryService.getById(1L);

        assertNotNull(response);
        assertEquals("T-Shirt", response.getName());
    }

    //GET ALL

    @Test
    @DisplayName("GetAll: Nên trả về danh sách tất cả category")
    void getAll_ShouldReturnAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(mockCategory));

        List<CategoryResponse> result = categoryService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    //DELETE

    @Test
    @DisplayName("Delete: Nên ném NotFoundException khi ID không tồn tại")
    void delete_ShouldThrowNotFoundException_WhenIdNotExists() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> categoryService.delete(99L));
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete: Nên xóa category khi ID tồn tại")
    void delete_ShouldDeleteCategory_WhenIdExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.delete(1L);

        verify(categoryRepository).deleteById(1L);
    }
}