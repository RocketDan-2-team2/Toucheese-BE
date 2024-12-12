package com.example.toucheese_be.domain.order.service;

import static com.example.toucheese_be.domain.order.entity.QOrderItem.orderItem;

import com.example.toucheese_be.domain.auth.user.entity.User;
import com.example.toucheese_be.domain.item.entity.Item;
import com.example.toucheese_be.domain.item.entity.ItemOption;
import com.example.toucheese_be.domain.item.entity.Option;
import com.example.toucheese_be.domain.item.repository.ItemOptionRepository;
import com.example.toucheese_be.domain.item.repository.ItemRepository;
import com.example.toucheese_be.domain.order.dto.request.OrderRequestDto;
import com.example.toucheese_be.domain.order.dto.request.OrderRequestItemDto;
import com.example.toucheese_be.domain.order.entity.Order;
import com.example.toucheese_be.domain.order.entity.OrderItem;
import com.example.toucheese_be.domain.order.entity.OrderOption;
import com.example.toucheese_be.domain.order.repository.OrderItemRepository;
import com.example.toucheese_be.domain.order.repository.OrderRepository;
import com.example.toucheese_be.domain.auth.user.repository.UserRepository;

import com.example.toucheese_be.domain.studio.entity.Studio;
import com.example.toucheese_be.domain.studio.repository.StudioRepository;
import com.example.toucheese_be.domain.toss.dto.PaymentConfirmDto;
import com.example.toucheese_be.domain.toss.service.TossHttpService;
import com.example.toucheese_be.global.error.ErrorCode;
import com.example.toucheese_be.global.error.GlobalCustomException;
import jakarta.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final TossHttpService tossHttpService;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


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
        User user = userRepository.save(User.builder()
                .username(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .build());

        Studio studio = studioRepository.findById(dto.getStudioId())
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.STUDIO_NOT_FOUND));

        List<Long> itemIds = dto.getOrderRequestItemDtos().stream()
                .map(OrderRequestItemDto::getItemId)
                .distinct()
                .collect(Collectors.toList());

        // Item 조회
        List<Item> items = itemRepository.findItemsByIds(itemIds);
        Map<Long, Item> itemMap = items.stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        // ItemOption 조회 (Item과 Option 조인)
        List<ItemOption> itemOptions = itemRepository.findItemOptionsWithOptions(itemIds);
        Map<Long, List<ItemOption>> optionMap = itemOptions.stream()
                .collect(Collectors.groupingBy(itemOption -> itemOption.getItem().getId()));

        // Option 조회 (Option 엔티티 가져오기)
        List<Long> itemOptionIds = itemOptions.stream()
                .map(ItemOption::getId)
                .collect(Collectors.toList());

        List<Option> options = itemRepository.findOptionsByItemOptionIds(itemOptionIds);
        Map<Long, Option> optionMapForItemOption = options.stream()
                .collect(Collectors.toMap(Option::getId, Function.identity()));

        Order order = Order.builder()
                .studio(studio)
                .user(user)
                .orderDateTime(dto.getOrderDateTime())
                .build();

        orderRepository.save(order);

        List<OrderItem> orderItems = dto.getOrderRequestItemDtos().stream()
                .map(itemRequest -> {
                    Long itemId = itemRequest.getItemId();
                    Item item = itemMap.get(itemId);

                    if (item == null) {
                        throw new GlobalCustomException(ErrorCode.ITEM_NOT_FOUND);
                    }

                    OrderItem orderItem = OrderItem.builder()
                            .item(item)
                            .name(item.getName())
                            .price(item.getPrice())
                            .quantity(itemRequest.getItemQuantity())
                            .order(order)
                            .build();

                    orderItemRepository.save(orderItem);

                    List<OrderOption> orderOptions = itemRequest.getOrderRequestOptionDtos().stream()
                            .map(optionRequest -> {
                                Long optionId = optionRequest.getOptionId();

                                List<ItemOption> matchedOptions = optionMap.get(itemId);
                                ItemOption matchedOption = matchedOptions.stream()
                                        .filter(opt -> opt.getId().equals(optionId))
                                        .findFirst()
                                        .orElseThrow(() -> new GlobalCustomException(ErrorCode.ITEM_OPTION_NOT_FOUND));

                                Option actualOption = optionMapForItemOption.get(matchedOption.getOption().getId());

                                return OrderOption.builder()
                                        .orderItem(orderItem)
                                        .itemOptionId(matchedOption)
                                        .name(actualOption.getName())
                                        .price(actualOption.getPrice())
                                        .quantity(optionRequest.getOptionQuantity())
                                        .build();

                            }).collect(Collectors.toList());

                    int orderOptionsTotalPrice = orderOptions.stream()
                            .mapToInt(opt -> opt.getPrice() * opt.getQuantity())
                            .sum();

                    int totalItemPrice = item.getPrice() * itemRequest.getItemQuantity() + orderOptionsTotalPrice;

                    orderItem.setTotalPrice(totalItemPrice);
                    orderItem.setOrder(order);
                    orderItem.setOrderOptions(orderOptions);
                    return orderItemRepository.save(orderItem);

                }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        return true;
    }

    // TODO: readTossPayment

    // TODO: cancelPayment

}
