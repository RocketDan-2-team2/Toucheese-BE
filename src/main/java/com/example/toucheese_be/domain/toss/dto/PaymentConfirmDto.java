package com.example.toucheese_be.domain.toss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmDto {
    // 결제 요청의 고유 키
    private String paymentKey;
    // 주문 고유 ID (Order 시스템 내 주문 정보)
    private String orderId;
    // 결제 금액
    private Integer amount;
}
