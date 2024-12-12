package com.example.toucheese_be.domain.order.repository;

import com.example.toucheese_be.domain.auth.admin.dto.AdminOrderDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.entity.OrderItem;
import com.example.toucheese_be.domain.order.entity.OrderOption;
import com.example.toucheese_be.domain.studio.dto.PageRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryCustom {
    Page<AdminOrderDto> findAllOrdersWithDetails(PageRequestDto dto);
    List<OrderItem> findOrderItemsByIds(List<Long> orderItemIds);
    List<OrderOption> findOrderOptionsByIds(List<Long> orderOptionIds);
}
