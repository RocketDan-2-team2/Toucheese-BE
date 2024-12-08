package com.example.toucheese_be.domain.order.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.toucheese_be.domain.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemDto {
    // 싱품 ID
    private Long itemId;
    // 상품 가격
    private String itemName;
    // 상품 이미지
    private String itemImage;
    // 상품 가격
    private Integer itemQuantity;
    // 최종 가격
    private Integer totalPrice;
    // 상품에 대한 옵션
    private List<OrderOptionDto> orderOptionDtos = new ArrayList<>();

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        return OrderItemDto.builder()
                .itemId(orderItem.getId())
                .itemName(orderItem.getItem().getName())
                .itemImage(orderItem.getItem().getImage())
                .itemQuantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}