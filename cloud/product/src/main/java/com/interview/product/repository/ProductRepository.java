package com.cloud.product.repository;

import com.cloud.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhangbaosheng
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Product> findByNameContainingIgnoreCase(@Param("name")String name, Pageable pageable);
}
