package com.example.toucheese_be.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 826131297L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> orderDateTime = createDateTime("orderDateTime", java.time.LocalDateTime.class);

    public final ListPath<OrderItem, QOrderItem> orderItems = this.<OrderItem, QOrderItem>createList("orderItems", OrderItem.class, QOrderItem.class, PathInits.DIRECT2);

    public final EnumPath<com.example.toucheese_be.domain.order.entity.constant.OrderStatus> status = createEnum("status", com.example.toucheese_be.domain.order.entity.constant.OrderStatus.class);

    public final com.example.toucheese_be.domain.studio.entity.QStudio studio;

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final com.example.toucheese_be.domain.user.entity.QUser user;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.studio = inits.isInitialized("studio") ? new com.example.toucheese_be.domain.studio.entity.QStudio(forProperty("studio"), inits.get("studio")) : null;
        this.user = inits.isInitialized("user") ? new com.example.toucheese_be.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

