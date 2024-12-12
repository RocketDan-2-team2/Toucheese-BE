package com.example.toucheese_be.domain.item.repository;

import com.example.toucheese_be.domain.item.entity.Item;
import com.example.toucheese_be.domain.item.entity.ItemOption;
import com.example.toucheese_be.domain.item.entity.Option;
import com.example.toucheese_be.domain.item.entity.QItem;
import com.example.toucheese_be.domain.item.entity.QItemOption;
import com.example.toucheese_be.domain.item.entity.QOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QItem item = QItem.item;
    private final QItemOption itemOption = QItemOption.itemOption;
    private final QOption option = QOption.option;


    @Override
    public List<Item> findItemsByIds(List<Long> itemIds) {
        return jpaQueryFactory.selectFrom(item)
                .where(item.id.in(itemIds))
                .fetch();
    }

    @Override
    public List<ItemOption> findItemOptionsWithOptions(List<Long> itemIds) {
        return jpaQueryFactory.selectFrom(itemOption)
                .join(itemOption.option, QOption.option)
                .where(itemOption.item.id.in(itemIds))
                .fetch();
    }
    @Override
    public List<Option> findOptionsByItemOptionIds(List<Long> itemOptionIds) {
        return jpaQueryFactory.selectFrom(option)
                .where(option.id.in(itemOptionIds))
                .fetch();
    }
}
