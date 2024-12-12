package com.example.toucheese_be.domain.order.repository;

import com.example.toucheese_be.domain.auth.admin.dto.AdminOrderDto;

import com.example.toucheese_be.domain.auth.admin.dto.AdminOrderItemDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.entity.OrderItem;
import com.example.toucheese_be.domain.order.entity.OrderOption;
import com.example.toucheese_be.domain.order.entity.QOrder;
import com.example.toucheese_be.domain.order.entity.QOrderItem;
import com.example.toucheese_be.domain.order.entity.QOrderOption;
import com.example.toucheese_be.domain.studio.dto.PageRequestDto;
import com.example.toucheese_be.domain.studio.entity.QStudio;
// import com.example.toucheese_be.domain.studio.entity.Studio;
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


    // 아직 미사용
    @Override
    public List<OrderItem> findOrderItemsByIds(List<Long> orderItemIds) {
        return jpaQueryFactory.selectFrom(orderItem)
                .where(orderItem.id.in(orderItemIds))
                .fetch();
    }

    // 아직 미사용
    @Override
    public List<OrderOption> findOrderOptionsByIds(List<Long> orderOptionIds) {
        return jpaQueryFactory.selectFrom(orderOption)
                .where(orderOption.id.in(orderOptionIds))
                .fetch();
    }



    @Override
    public Page<AdminOrderDto> findAllOrdersWithDetails(PageRequestDto dto) {
        Pageable pageable = dto.toPageable();

        // 1. 주문과 주문 상품 가져오기
        List<Order> ordersWithItems = jpaQueryFactory.selectFrom(order)
                .distinct()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 주문 상품과 주문 옵션를 가져오기
        List<OrderItem> orderItemsWithOptions = jpaQueryFactory.selectFrom(orderItem)
                .leftJoin(orderItem.orderOptions, orderOption).fetchJoin()
                .fetch();

        // 3. Map<주문ID, 주문 상품 리스트> 만들기
        Map<Long, List<OrderItem>> orderItemsMap = orderItemsWithOptions.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getId()));

        // 3. AdminOrderDto로 변환
        List<AdminOrderDto> adminOrderDtos = ordersWithItems.stream().map(orderEntity -> AdminOrderDto.builder()
                        .orderId(orderEntity.getId())
                        .studioName(orderEntity.getStudio().getName())
                        .studioProfile(orderEntity.getStudio().getImages().stream()
                                .filter(studioImage -> studioImage.getType().equals(StudioImageType.PROFILE))
                                .toList().get(0).getImageUrl())
                        .orderDateTime(orderEntity.getOrderDateTime().toString())
                        .userName(orderEntity.getUser().getUsername())
                        .orderItems(orderItemsMap.getOrDefault(orderEntity.getId(), Collections.emptyList()).stream()
                                .map(AdminOrderItemDto::fromEntity)
                                .collect(Collectors.toList()))
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
