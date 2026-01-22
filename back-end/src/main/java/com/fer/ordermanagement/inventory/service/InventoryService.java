package com.fer.ordermanagement.inventory.service;

import com.fer.ordermanagement.product.entity.Product;

public interface InventoryService {
    void createForProduct(Product product);

    void increase(Long productId, int amount);

    void reserve(Long productId, int amount);

    void release(Long productId, int amount);
}
