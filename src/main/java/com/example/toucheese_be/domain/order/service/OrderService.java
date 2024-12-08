package com.example.toucheese_be.domain.order.service;

import com.example.toucheese_be.domain.order.dto.OrderDetailDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.repository.OrderRepository;
import com.example.toucheese_be.domain.studio.dto.ConceptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    // TODO: 주문 생성 컨트롤러 메서드에 대한 서비스 로직
    private final OrderRepository orderRepository;

    // 주문서 조회
    public Page<OrderDetailDto> getOrders(Long orderId, Pageable pageable) {
        // 주문 ID로 주문을 조회
        Page<Order> orders = orderRepository.findOrdersWithDetails(orderId, pageable);
        // 엔티티를 DTO로 변환
        return orders.map(this::convertToDto);
    }

    // Order 엔티티를 OrderDetailDto로 변환하는 메서드
    private OrderDetailDto convertToDto(Order order) {
        return OrderDetailDto.fromEntity(order);
    }
}
