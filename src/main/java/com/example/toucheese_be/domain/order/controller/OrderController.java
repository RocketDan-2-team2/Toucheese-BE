package com.example.toucheese_be.domain.order.controller;

import com.example.toucheese_be.domain.order.dto.OrderDetailDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.service.OrderService;
import com.example.toucheese_be.domain.studio.dto.StudioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    // TODO: 주문을 만드는 주문 생성 컨트롤러

    private final OrderService orderService;

    @GetMapping("/create")
    public ResponseEntity<Page<OrderDetailDto>> getOrderDetails(@RequestParam Long orderId, Pageable pageable) {
        Page<OrderDetailDto> orderDetails = orderService.getOrders(orderId, pageable);
        return ResponseEntity.ok(orderDetails);
    }
}