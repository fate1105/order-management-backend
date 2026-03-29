package com.fer.ordermanagement.product.controller;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import com.fer.ordermanagement.product.controller.api.ProductApi;
import com.fer.ordermanagement.product.dto.ProductCreateRequest;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.dto.ProductUpdateRequest;
import com.fer.ordermanagement.product.enums.ProductStatus;
import com.fer.ordermanagement.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> create(ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(productService.create(request))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> update(Long id, ProductUpdateRequest request) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.update(id, request))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> getById(Long id) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.getById(id))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponse<ProductResponse>>> getAll(
            int page, int size, String keyword, ProductStatus status
    ) {
        Page<ProductResponse> result = productService.getAllPaged(page, size, keyword, status);
        return ResponseEntity.ok(
                BaseResponse.success(new PageResponse<>(result))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(Long id) {
        productService.delete(id);
        return ResponseEntity.ok(
                BaseResponse.success("Xóa sản phẩm thành công")
        );
    }
}