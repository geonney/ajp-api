package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 678284994L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    public final StringPath accessToken = createString("accessToken");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final QAuthority authority;

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final StringPath cellphone = createString("cellphone");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Double> height = createNumber("height", Double.class);

    public final NumberPath<Integer> loginAttemptsCount = createNumber("loginAttemptsCount", Integer.class);

    public final StringPath memberId = createString("memberId");

    public final StringPath memberName = createString("memberName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath password = createString("password");

    public final DatePath<java.time.LocalDate> passwordUpdateDate = createDate("passwordUpdateDate", java.time.LocalDate.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final QMemberTeam team;

    public final EnumPath<com.aljjabaegi.api.entity.enumerated.UseYn> useYn = createEnum("useYn", com.aljjabaegi.api.entity.enumerated.UseYn.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.authority = inits.isInitialized("authority") ? new QAuthority(forProperty("authority")) : null;
        this.team = inits.isInitialized("team") ? new QMemberTeam(forProperty("team"), inits.get("team")) : null;
    }

}

