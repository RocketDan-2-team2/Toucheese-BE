package com.example.toucheese_be.domain.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -252492063L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final EnumPath<com.example.toucheese_be.domain.item.entity.constant.ItemCategory> itemCategory = createEnum("itemCategory", com.example.toucheese_be.domain.item.entity.constant.ItemCategory.class);

    public final ListPath<ItemOption, QItemOption> itemOptions = this.<ItemOption, QItemOption>createList("itemOptions", ItemOption.class, QItemOption.class, PathInits.DIRECT2);

    public final ListPath<com.example.toucheese_be.domain.review.entity.Review, com.example.toucheese_be.domain.review.entity.QReview> itemReview = this.<com.example.toucheese_be.domain.review.entity.Review, com.example.toucheese_be.domain.review.entity.QReview>createList("itemReview", com.example.toucheese_be.domain.review.entity.Review.class, com.example.toucheese_be.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.example.toucheese_be.domain.studio.entity.QStudio studio;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.studio = inits.isInitialized("studio") ? new com.example.toucheese_be.domain.studio.entity.QStudio(forProperty("studio"), inits.get("studio")) : null;
    }

}

