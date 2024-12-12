package com.example.toucheese_be.domain.item.repository;

import com.example.toucheese_be.domain.item.entity.Item;
import com.example.toucheese_be.domain.item.entity.ItemOption;
import com.example.toucheese_be.domain.item.entity.Option;
import com.example.toucheese_be.domain.order.dto.request.OrderRequestItemDto;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {
    List<Item> findItemsByIds(List<Long> itemIds);
    List<ItemOption> findItemOptionsWithOptions(List<Long> itemIds);
    List<Option> findOptionsByItemOptionIds(List<Long> itemOptionIds);
}
