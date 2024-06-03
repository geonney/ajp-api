package com.aljjabaegi.api.entity.code;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QResponsibilityCode is a Querydsl query type for ResponsibilityCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QResponsibilityCode extends EntityPathBase<ResponsibilityCode> {

    private static final long serialVersionUID = -1361942572L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResponsibilityCode responsibilityCode = new QResponsibilityCode("responsibilityCode");

    public final com.aljjabaegi.api.entity.key.QCodeKey codeKey;

    public final StringPath codeName = createString("codeName");

    public QResponsibilityCode(String variable) {
        this(ResponsibilityCode.class, forVariable(variable), INITS);
    }

    public QResponsibilityCode(Path<? extends ResponsibilityCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResponsibilityCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResponsibilityCode(PathMetadata metadata, PathInits inits) {
        this(ResponsibilityCode.class, metadata, inits);
    }

    public QResponsibilityCode(Class<? extends ResponsibilityCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.codeKey = inits.isInitialized("codeKey") ? new com.aljjabaegi.api.entity.key.QCodeKey(forProperty("codeKey")) : null;
    }

}

