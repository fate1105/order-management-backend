package com.fer.ordermanagement.product.service;

import com.fer.ordermanagement.inventory.service.InventoryService;
import com.fer.ordermanagement.product.dto.ProductCreateRequest;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.dto.ProductUpdateRequest;
import com.fer.ordermanagement.category.entity.Category;
import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.mapper.ProductMapper;
import com.fer.ordermanagement.category.repository.CategoryRepository;
import com.fer.ordermanagement.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public ProductResponse create(ProductCreateRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU already exists: " + request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        productRepository.save(product);
        inventoryService.createForProduct(product);
        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest req) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + req.getCategoryId()));

        product.setName(req.getName());
        product.setPrice(req.getPrice());
        product.setDescription(req.getDescription());
        product.setStatus(req.getStatus());
        product.setCategory(category);

        return ProductMapper.toResponse(product);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return ProductMapper.toResponse(p);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
