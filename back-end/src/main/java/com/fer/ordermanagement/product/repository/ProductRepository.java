package com.fer.ordermanagement.product.repository;

import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("""
        SELECT p FROM Product p
        JOIN FETCH p.category
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithCategory(Long id);

    @Query("""
        SELECT DISTINCT p FROM Product p
        JOIN FETCH p.category
    """)
    List<Product> findAllWithCategory();

    @Query(
            value = """
        SELECT p
        FROM Product p
        JOIN FETCH p.category c
        WHERE
            (:keyword IS NULL OR
                LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        AND (:status IS NULL OR p.status = :status)
    """,
            countQuery = """
        SELECT COUNT(p)
        FROM Product p
        JOIN p.category c
        WHERE
            (:keyword IS NULL OR
                LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        AND (:status IS NULL OR p.status = :status)
    """
    )
    Page<Product> searchWithPaging(
            @Param("keyword") String keyword,
            @Param("status") ProductStatus status,
            Pageable pageable
    );
}
