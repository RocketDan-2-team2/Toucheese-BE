package com.example.toucheese_be.domain.order.repository;

import com.example.toucheese_be.domain.order.dto.OrderDetailDto;
import com.example.toucheese_be.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("SELECT o FROM Order o WHERE o.id = ?1")
    Page<Order> findOrdersWithDetails(Long orderId, Pageable pageable);
}
