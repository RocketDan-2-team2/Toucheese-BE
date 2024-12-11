package com.example.toucheese_be.domain.studio.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConcept is a Querydsl query type for Concept
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConcept extends EntityPathBase<Concept> {

    private static final long serialVersionUID = 1413936695L;

    public static final QConcept concept = new QConcept("concept");

    public final StringPath conceptImg = createString("conceptImg");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<Studio, QStudio> studios = this.<Studio, QStudio>createList("studios", Studio.class, QStudio.class, PathInits.DIRECT2);

    public QConcept(String variable) {
        super(Concept.class, forVariable(variable));
    }

    public QConcept(Path<? extends Concept> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConcept(PathMetadata metadata) {
        super(Concept.class, metadata);
    }

}

