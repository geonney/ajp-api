package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCode is a Querydsl query type for Code
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCode extends EntityPathBase<Code> {

    private static final long serialVersionUID = 1336728597L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCode code = new QCode("code");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    public final StringPath codeDescription = createString("codeDescription");

    public final QCodeGroup codeGroup;

    public final StringPath codeName = createString("codeName");

    public final NumberPath<Integer> codeOrder = createNumber("codeOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final com.aljjabaegi.api.entity.key.QCodeKey key;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final EnumPath<com.aljjabaegi.api.entity.enumerated.UseYn> useYn = createEnum("useYn", com.aljjabaegi.api.entity.enumerated.UseYn.class);

    public QCode(String variable) {
        this(Code.class, forVariable(variable), INITS);
    }

    public QCode(Path<? extends Code> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCode(PathMetadata metadata, PathInits inits) {
        this(Code.class, metadata, inits);
    }

    public QCode(Class<? extends Code> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.codeGroup = inits.isInitialized("codeGroup") ? new QCodeGroup(forProperty("codeGroup")) : null;
        this.key = inits.isInitialized("key") ? new com.aljjabaegi.api.entity.key.QCodeKey(forProperty("key")) : null;
    }

}

