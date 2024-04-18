package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = 1337225349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final QMember leader;

    public final ListPath<Member, QMember> members = this.<Member, QMember>createList("members", Member.class, QMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final StringPath teamName = createString("teamName");

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.leader = inits.isInitialized("leader") ? new QMember(forProperty("leader"), inits.get("leader")) : null;
    }

}

