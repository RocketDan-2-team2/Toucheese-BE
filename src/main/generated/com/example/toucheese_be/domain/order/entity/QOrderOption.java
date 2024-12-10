package com.example.toucheese_be.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderOption is a Querydsl query type for OrderOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderOption extends EntityPathBase<OrderOption> {

    private static final long serialVersionUID = -2029294794L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderOption orderOption = new QOrderOption("orderOption");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.toucheese_be.domain.item.entity.QItemOption itemOption;

    public final StringPath name = createString("name");

    public final QOrderItem orderItem;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QOrderOption(String variable) {
        this(OrderOption.class, forVariable(variable), INITS);
    }

    public QOrderOption(Path<? extends OrderOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderOption(PathMetadata metadata, PathInits inits) {
        this(OrderOption.class, metadata, inits);
    }

    public QOrderOption(Class<? extends OrderOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.itemOption = inits.isInitialized("itemOption") ? new com.example.toucheese_be.domain.item.entity.QItemOption(forProperty("itemOption"), inits.get("itemOption")) : null;
        this.orderItem = inits.isInitialized("orderItem") ? new QOrderItem(forProperty("orderItem"), inits.get("orderItem")) : null;
    }

}

