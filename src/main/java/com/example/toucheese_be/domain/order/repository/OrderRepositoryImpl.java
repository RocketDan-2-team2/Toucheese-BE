package com.example.toucheese_be.domain.order.repository;

import static com.example.toucheese_be.domain.auth.user.entity.QUser.user;

import com.example.toucheese_be.domain.auth.admin.dto.AdminOrderDto;

import com.example.toucheese_be.domain.auth.admin.dto.AdminOrderItemDto;
import com.example.toucheese_be.domain.auth.admin.dto.AdminOrderOptionDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.entity.OrderItem;
import com.example.toucheese_be.domain.order.entity.OrderOption;
import com.example.toucheese_be.domain.order.entity.QOrder;
import com.example.toucheese_be.domain.order.entity.QOrderItem;
import com.example.toucheese_be.domain.order.entity.QOrderOption;
import com.example.toucheese_be.domain.studio.dto.PageRequestDto;
import com.example.toucheese_be.domain.studio.entity.QStudio;
import com.example.toucheese_be.domain.studio.entity.Studio;
import com.example.toucheese_be.domain.studio.entity.StudioImage;
import com.example.toucheese_be.domain.studio.entity.constant.StudioImageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QOrder order = QOrder.order;
    private final QOrderItem orderItem = QOrderItem.orderItem;
    private final QOrderOption orderOption = QOrderOption.orderOption;
    private final QStudio studio = QStudio.studio;

    @Override
    public Page<AdminOrderDto> findAllOrdersWithDetails(PageRequestDto dto) {
        Pageable pageable = dto.toPageable();

        // 1. Order 와 관련 데이터 (Studio, User 만 fetchJoin)
        List<Order> orders = jpaQueryFactory.selectFrom(order)
                .join(order.user, user).fetchJoin()
                .join(order.studio, studio).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. OrderItem 만 조회 (Order ID를 기준으로)
        List<OrderItem> orderItems = jpaQueryFactory.selectFrom(orderItem)
                .join(orderItem.order, order)
                .where(order.id.in(orders.stream().map(Order::getId).toList()))
                .fetch();

        // 3. OrderOption 만 조회 (OrderItem ID를 기준으로)
        List<OrderOption> orderOptions = jpaQueryFactory.selectFrom(orderOption)
                .join(orderOption.orderItem, orderItem)
                .where(orderItem.id.in(orderItems.stream().map(OrderItem::getId).toList()))
                .fetch();

        // 4. 데이터 조합
        Map<Long, List<AdminOrderItemDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(
                        orderItem -> orderItem.getOrder().getId(),
                        Collectors.mapping(
                                item -> AdminOrderItemDto.builder()
                                        .itemId(item.getId())
                                        .itemName(item.getName())
                                        //.itemName(item.getItem().getName())
                                        .adminOrderOptions(orderOptions.stream()
                                                .filter(option -> option.getOrderItem().getId().equals(item.getId()))
                                                .map(option -> new AdminOrderOptionDto(
                                                        option.getId(), option.getName(), option.getPrice(), option.getQuantity()))
                                                        //option.getId(), option.getItemOption().getOption().getName(),option.getPrice(), option.getQuantity()))
                                                .collect(Collectors.toList()))
                                        .build(),
                                Collectors.toList()
                        )
                ));

        // 5. 최종 DTO 매핑 - AdminOrderDto 생성
        List<AdminOrderDto> adminOrderDtos = orders.stream()
                .map(orderEntity -> AdminOrderDto.builder()
                        .orderId(orderEntity.getId())
                        .studioName(orderEntity.getStudio().getName())
                        .studioProfile(orderEntity.getStudio().getImages().stream()
                                .filter(studioImage -> studioImage.getType().equals(StudioImageType.PROFILE))
                                .findFirst()
                                .map(StudioImage::getImageUrl)
                                .orElse(null))
                        .orderDateTime(orderEntity.getOrderDateTime().toString())
                        .userName(orderEntity.getUser().getName())
                        .orderItems(orderItemMap.getOrDefault(orderEntity.getId(), Collections.emptyList()))
                        .build())
                .collect(Collectors.toList());

        // 4. 총 카운트 조회
        Long total = Optional.ofNullable(
                jpaQueryFactory.select(order.count())
                        .from(order)
                        .fetchOne()
        ).orElse(0L);

        // 5. Page 객체로 반환
        return new PageImpl<>(adminOrderDtos, pageable, total);
    }
}
