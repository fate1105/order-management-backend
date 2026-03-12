package com.fer.ordermanagement.inventory.service;

import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.inventory.entity.Inventory;
import com.fer.ordermanagement.inventory.repository.InventoryRepository;
import com.fer.ordermanagement.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public void createForProduct(Product product) {
        if (inventoryRepository.existsByProductId(product.getId())) {
            return;
        }
        Inventory inventory = Inventory.create(product);
        inventoryRepository.save(inventory);
    }

    @Override
    public void increase(Long productId, int amount) {
        getInventoryForUpdate(productId).increase(amount);
    }

    @Override
    public void reserve(Long productId, int amount) {
        getInventoryForUpdate(productId).reserve(amount);
    }

    @Override
    public void release(Long productId, int amount) {
        getInventoryForUpdate(productId).release(amount);
    }

    private Inventory getInventoryForUpdate(Long productId) {
        return inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException("Inventory not found for product: " + productId));
    }
}