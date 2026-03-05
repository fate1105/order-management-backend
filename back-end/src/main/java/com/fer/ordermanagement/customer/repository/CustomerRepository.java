package com.fer.ordermanagement.customer.repository;

import com.fer.ordermanagement.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone, Long customerId);
    boolean existsByEmailAndIdNot(String email, Long customerId);

    @Query(
            value = """
        SELECT c 
        FROM Customer c 
        WHERE
            (:keyword IS NULL OR
                LOWER(c.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )               
        """,
            countQuery = """
        SELECT COUNT(c)
        FROM Customer c
        WHERE
            (:keyword IS NULL OR
                LOWER(c.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """
    )
    Page<Customer> searchWithPaging(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
