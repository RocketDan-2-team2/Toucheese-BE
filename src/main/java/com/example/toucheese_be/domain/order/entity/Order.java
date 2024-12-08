package com.example.toucheese_be.domain.order.entity;

import com.example.toucheese_be.domain.order.dto.OrderUserDto;
import com.example.toucheese_be.domain.order.entity.constant.OrderStatus;
import com.example.toucheese_be.domain.auth.user.entity.User;
import com.example.toucheese_be.domain.studio.entity.Studio;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
/* 주문 엔티티 */
public class Order {
    // 주문 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Studio과 연결
    @ManyToOne
    @JoinColumn(name = "studio_id", nullable = false)
    private Studio studio;

    // 사용자와 연결
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 주문 시간
    private LocalDateTime orderDateTime;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.KEEP;

    // 주문 상품과 연결
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


}
