package com.orishop.repository;

import com.orishop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(com.orishop.model.User user);

    List<Order> findTop5ByOrderByCreatedAtDesc();

    List<Order> findByStatus(com.orishop.model.OrderStatus status);
}
