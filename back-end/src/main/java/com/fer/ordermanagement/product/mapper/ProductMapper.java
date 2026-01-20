package com.fer.ordermanagement.product.mapper;

import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.entity.Product;

public class ProductMapper {
    public static ProductResponse toResponse(Product product){
        ProductResponse res = new ProductResponse();

        res.setId(product.getId());
        res.setSku(product.getSku());
        res.setName(product.getName());
        res.setPrice(product.getPrice());
        res.setDescription(product.getDescription());
        res.setStatus(product.getStatus().name());

        res.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);

        res.setCreatedAt(product.getCreatedAt());
        res.setUpdatedAt(product.getUpdatedAt());
        return res;
    }
}
