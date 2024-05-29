package com.aljjabaegi.api.entity.key;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeKey is a Querydsl query type for CodeKey
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCodeKey extends BeanPath<CodeKey> {

    private static final long serialVersionUID = 726792571L;

    public static final QCodeKey codeKey = new QCodeKey("codeKey");

    public final StringPath codeGroupId = createString("codeGroupId");

    public final StringPath codeId = createString("codeId");

    public QCodeKey(String variable) {
        super(CodeKey.class, forVariable(variable));
    }

    public QCodeKey(Path<? extends CodeKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeKey(PathMetadata metadata) {
        super(CodeKey.class, metadata);
    }

}

