package com.orishop.repository;

import com.orishop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.Optional<Product> findBySlug(String slug);

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    List<Product> searchByName(@Param("keyword") String keyword);

    @Query("SELECT oi.product FROM OrderItem oi WHERE oi.order.status = :status GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<Product> findBestSellingProducts(@Param("status") com.orishop.model.OrderStatus status, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}
