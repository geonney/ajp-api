package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCodeGroup is a Querydsl query type for CodeGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeGroup extends EntityPathBase<CodeGroup> {

    private static final long serialVersionUID = 770731882L;

    public static final QCodeGroup codeGroup = new QCodeGroup("codeGroup");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    public final StringPath codeGroupDescription = createString("codeGroupDescription");

    public final StringPath codeGroupId = createString("codeGroupId");

    public final StringPath codeGroupName = createString("codeGroupName");

    public final ListPath<Code, QCode> codes = this.<Code, QCode>createList("codes", Code.class, QCode.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QCodeGroup(String variable) {
        super(CodeGroup.class, forVariable(variable));
    }

    public QCodeGroup(Path<? extends CodeGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeGroup(PathMetadata metadata) {
        super(CodeGroup.class, metadata);
    }

}

