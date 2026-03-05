package com.fer.ordermanagement.product.service;

import com.fer.ordermanagement.product.dto.ProductCreateRequest;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.dto.ProductUpdateRequest;
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.enums.ProductStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductCreateRequest req);
    ProductResponse update(Long id, ProductUpdateRequest req);
    ProductResponse getById(Long id);
    List<ProductResponse> getAll();
    void delete(Long id);

    Page<ProductResponse> getAllPaged(int page, int size, String keyword, ProductStatus status);
}
