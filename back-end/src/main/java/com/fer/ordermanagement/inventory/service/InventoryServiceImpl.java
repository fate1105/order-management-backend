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
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepository;

    @Override
    public void createForProduct(Product product){
        if(inventoryRepository.existsByProductId(product.getId())){
            return;
        }
        Inventory inventory = Inventory.create(product);
        inventoryRepository.save(inventory);
    }

    @Override
    public void increase(Long productId, int amount){
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException("Inventory not found for " + productId));
        inventory.increase(amount);
    }

    @Override
    public void reserve(Long productId, int amount){
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException("Inventory not found for " + productId));
        inventory.reserve(amount);
    }

    @Override
    public void release(Long productId, int amount){
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException("Inventory not found for " + productId));
        inventory.release(amount);
    }
}
