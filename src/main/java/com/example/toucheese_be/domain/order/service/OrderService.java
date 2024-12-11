package com.example.toucheese_be.domain.order.service;

import com.example.toucheese_be.domain.auth.user.entity.User;
import com.example.toucheese_be.domain.item.entity.Item;
import com.example.toucheese_be.domain.item.entity.ItemOption;
import com.example.toucheese_be.domain.item.repository.ItemOptionRepository;
import com.example.toucheese_be.domain.item.repository.ItemRepository;
import com.example.toucheese_be.domain.order.dto.request.OrderRequestDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.entity.OrderItem;
import com.example.toucheese_be.domain.order.entity.OrderOption;
import com.example.toucheese_be.domain.order.repository.OrderRepository;
import com.example.toucheese_be.domain.auth.user.repository.UserRepository;

import com.example.toucheese_be.domain.studio.entity.Studio;
import com.example.toucheese_be.domain.studio.repository.StudioRepository;
import com.example.toucheese_be.domain.toss.dto.PaymentConfirmDto;
import com.example.toucheese_be.domain.toss.service.TossHttpService;
import com.example.toucheese_be.global.error.ErrorCode;
import com.example.toucheese_be.global.error.GlobalCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final TossHttpService tossHttpService;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public void confirmPayment(PaymentConfirmDto dto) {
        Object tossPaymentObj = tossHttpService.confirmPayment(dto);
        // 2. 응답 데이터에서 결제 정보 추출
        LinkedHashMap<String, Object> tossData = (LinkedHashMap<String, Object>) tossPaymentObj;
        String orderName = tossData.get("orderName").toString();
        // orderName 에서 itemId 회수 하여 이에 해당하는 Item 엔티티 조회
        // Item 엔티티를 바탕으로 OrderItem 만들기

        // 이떄 결제 성공하였고 결제 status 를 true 로 변경 후 리턴

    }


    // TODO: 추후 querydsl 로 쿼리 성능 최적화할 예정
    // TODO: boolean 에서 orderId 와 amount 르 반환하는 로직으로 변경해야함
    // TODO : 프론트엔드가 Toss 서버로 요청을 보낼 때, Authorization 헤더 값으로 Bearer Token이 필요 -> 응답의 Hedaer 로 제공하자
    @Transactional
    public boolean createOrder(OrderRequestDto dto) {
        // 사용자 정보 생성 및 저장
        User user = userRepository.save(User.builder()
                .username(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .build());

        // 스튜디오 조회
        Studio studio = studioRepository.findById(dto.getStudioId())
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.STUDIO_NOT_FOUND));

        // 주문 생성
        Order order = Order.builder()
                .studio(studio)
                .user(user)
                .orderDateTime(dto.getOrderDateTime())
                .build();

        // 주문 상품 생성
        List<OrderItem> orderItems = dto.getOrderRequestItemDtos().stream()
                .map(orderRequestItemDto -> {
                    // item 조회
                    Item item = itemRepository.findById(orderRequestItemDto.getItemId())
                            .orElseThrow(() -> new GlobalCustomException(ErrorCode.ITEM_NOT_FOUND));

                    // 옵션 처리
                    List<OrderOption> orderOptions = orderRequestItemDto.getOrderRequestOptionDtos().stream()
                            .map(orderRequestOptionDto -> {
                                ItemOption itemOption = itemOptionRepository.findById(orderRequestOptionDto.getOptionId())
                                        .orElseThrow(() -> new GlobalCustomException(ErrorCode.ITEM_OPTION_NOT_FOUND));

                                return OrderOption.builder()
                                        .itemOptionId(itemOption)
                                        .name(itemOption.getOption().getName())
                                        .price(itemOption.getOption().getPrice())
                                        .quantity(orderRequestOptionDto.getOptionQuantity())
                                        .build();
                            })
                            .toList();

                    int orderOptionsTotalPrice = orderOptions.stream()
                            .mapToInt(option -> option.getPrice() * option.getQuantity())
                            .sum();

                    int totalOrderItemPrice =
                            item.getPrice() * orderRequestItemDto.getItemQuantity() + orderOptionsTotalPrice;

                    // OrderItem 생성
                    return OrderItem.builder()
                            .item(item)
                            .name(item.getName())
                            .price(item.getPrice())
                            .quantity(orderRequestItemDto.getItemQuantity())
                            .totalPrice(totalOrderItemPrice)
                            .order(order)
                            .build();
                })
                .toList();

        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return true;
    }

    // TODO: readTossPayment

    // TODO: cancelPayment

}
