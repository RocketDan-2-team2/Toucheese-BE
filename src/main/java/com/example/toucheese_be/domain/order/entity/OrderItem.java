package com.example.toucheese_be.domain.order.entity;

import com.example.toucheese_be.domain.item.entity.Item;
import com.example.toucheese_be.domain.studio.entity.Studio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
/* 주문 상품 엔티티 */
public class OrderItem {
    // 주문 상품 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order와 연결
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Item과 연결
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // 상품 이름
    private String name;

    // 상품 수량
    private Integer quantity;

    // 상품 총 가격
    private Integer totalPrice;

    // 주문 옵션과 연결
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<OrderOption> orderOptions = new ArrayList<>();
}
