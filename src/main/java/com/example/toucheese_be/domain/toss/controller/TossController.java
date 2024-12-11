package com.example.toucheese_be.domain.toss.controller;

import com.example.toucheese_be.domain.order.service.OrderService;
import com.example.toucheese_be.domain.toss.dto.PaymentConfirmDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/toss")
@RequiredArgsConstructor
public class TossController {
    private final OrderService orderService;

    @PostMapping("/confirm-payment")
    public void confirmPayment(
            @RequestBody
            PaymentConfirmDto dto
    ) {
        orderService.confirmPayment(dto);
    }
}
