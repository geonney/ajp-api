package com.aljjabaegi.api.entity.key;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHistoryLoginKey is a Querydsl query type for HistoryLoginKey
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QHistoryLoginKey extends BeanPath<HistoryLoginKey> {

    private static final long serialVersionUID = -828009933L;

    public static final QHistoryLoginKey historyLoginKey = new QHistoryLoginKey("historyLoginKey");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath memberId = createString("memberId");

    public QHistoryLoginKey(String variable) {
        super(HistoryLoginKey.class, forVariable(variable));
    }

    public QHistoryLoginKey(Path<? extends HistoryLoginKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHistoryLoginKey(PathMetadata metadata) {
        super(HistoryLoginKey.class, metadata);
    }

}

