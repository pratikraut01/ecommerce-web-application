package com.example.shop.repository;

import com.example.shop.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<CustomerOrder> findAllByOrderByCreatedAtDesc();
}
