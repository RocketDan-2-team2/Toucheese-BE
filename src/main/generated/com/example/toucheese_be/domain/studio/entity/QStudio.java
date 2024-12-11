package com.example.toucheese_be.domain.studio.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudio is a Querydsl query type for Studio
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudio extends EntityPathBase<Studio> {

    private static final long serialVersionUID = 92862503L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudio studio = new QStudio("studio");

    public final StringPath address = createString("address");

    public final QConcept concept;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<StudioImage, QStudioImage> images = this.<StudioImage, QStudioImage>createList("images", StudioImage.class, QStudioImage.class, PathInits.DIRECT2);

    public final ListPath<com.example.toucheese_be.domain.item.entity.Item, com.example.toucheese_be.domain.item.entity.QItem> items = this.<com.example.toucheese_be.domain.item.entity.Item, com.example.toucheese_be.domain.item.entity.QItem>createList("items", com.example.toucheese_be.domain.item.entity.Item.class, com.example.toucheese_be.domain.item.entity.QItem.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final NumberPath<Double> popularity = createNumber("popularity", Double.class);

    public final ListPath<Portfolio, QPortfolio> portfolios = this.<Portfolio, QPortfolio>createList("portfolios", Portfolio.class, QPortfolio.class, PathInits.DIRECT2);

    public final ListPath<StudioDutyDate, QStudioDutyDate> studioDutyDates = this.<StudioDutyDate, QStudioDutyDate>createList("studioDutyDates", StudioDutyDate.class, QStudioDutyDate.class, PathInits.DIRECT2);

    public QStudio(String variable) {
        this(Studio.class, forVariable(variable), INITS);
    }

    public QStudio(Path<? extends Studio> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudio(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudio(PathMetadata metadata, PathInits inits) {
        this(Studio.class, metadata, inits);
    }

    public QStudio(Class<? extends Studio> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.concept = inits.isInitialized("concept") ? new QConcept(forProperty("concept")) : null;
    }

}

