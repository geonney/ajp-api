package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthority is a Querydsl query type for Authority
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthority extends EntityPathBase<Authority> {

    private static final long serialVersionUID = -899386853L;

    public static final QAuthority authority = new QAuthority("authority");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    public final StringPath authorityCode = createString("authorityCode");

    public final StringPath authorityName = createString("authorityName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<Member, QMember> members = this.<Member, QMember>createList("members", Member.class, QMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QAuthority(String variable) {
        super(Authority.class, forVariable(variable));
    }

    public QAuthority(Path<? extends Authority> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthority(PathMetadata metadata) {
        super(Authority.class, metadata);
    }

}

