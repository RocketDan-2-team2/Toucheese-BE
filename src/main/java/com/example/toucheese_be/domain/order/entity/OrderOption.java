package com.example.toucheese_be.domain.order.entity;

import com.example.toucheese_be.domain.item.entity.ItemOption;
import com.example.toucheese_be.domain.item.entity.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
/* 주문 옵션 엔티티 */
public class OrderOption {
    // 주문 옵션 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 상품과 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    // ItemOption(상품-옵션) 테이블과 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id", nullable = false)
    private ItemOption itemOption;

    // 옵션 이름
    private String name;

    // 옵션 수량
    private Integer quantity;

    // 옵션 가격
    private Integer price;
}
