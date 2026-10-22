package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /** Search by keyword + filter by category and price range. catId = 0 means "all categories". */
    @Query("""
           SELECT p FROM Product p
           WHERE p.active = true
             AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')))
             AND (:catId = 0 OR p.category.id = :catId)
             AND p.price >= :min AND p.price <= :max
           """)
    Page<Product> search(@Param("q") String q, @Param("catId") Long catId,
                         @Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);

    List<Product> findByActiveTrueOrderByIdDesc();
}
