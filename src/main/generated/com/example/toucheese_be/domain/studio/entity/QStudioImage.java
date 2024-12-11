package com.example.toucheese_be.domain.studio.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudioImage is a Querydsl query type for StudioImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudioImage extends EntityPathBase<StudioImage> {

    private static final long serialVersionUID = -1474903692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudioImage studioImage = new QStudioImage("studioImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> position = createNumber("position", Integer.class);

    public final QStudio studio;

    public final EnumPath<com.example.toucheese_be.domain.studio.entity.constant.StudioImageType> type = createEnum("type", com.example.toucheese_be.domain.studio.entity.constant.StudioImageType.class);

    public QStudioImage(String variable) {
        this(StudioImage.class, forVariable(variable), INITS);
    }

    public QStudioImage(Path<? extends StudioImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudioImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudioImage(PathMetadata metadata, PathInits inits) {
        this(StudioImage.class, metadata, inits);
    }

    public QStudioImage(Class<? extends StudioImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.studio = inits.isInitialized("studio") ? new QStudio(forProperty("studio"), inits.get("studio")) : null;
    }

}

