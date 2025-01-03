package com.example.toucheese_be.domain.order.entity;

import com.example.toucheese_be.domain.order.dto.OrderDetailDto;
import com.example.toucheese_be.domain.order.dto.OrderItemDto;
import com.example.toucheese_be.domain.order.dto.OrderOptionDto;
import com.example.toucheese_be.domain.order.dto.OrderUserDto;
import com.example.toucheese_be.domain.order.entity.constant.OrderStatus;
import com.example.toucheese_be.domain.user.entity.User;
import com.example.toucheese_be.domain.studio.entity.Studio;
import io.micrometer.common.KeyValues;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
/* 주문 엔티티 */
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDateTime;
    private Integer totalPrice;

    // Studio과 연결
    @ManyToOne
    @JoinColumn(name = "studio_id", nullable = false)
    private Studio studio;

    // 사용자와 연결
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.KEEP_RESERVATION;

    // 주문 상품과 연결
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<OrderItem> orderItems;
}

