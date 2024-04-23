package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenuAuthority is a Querydsl query type for MenuAuthority
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuAuthority extends EntityPathBase<MenuAuthority> {

    private static final long serialVersionUID = -1574245508L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenuAuthority menuAuthority = new QMenuAuthority("menuAuthority");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    public final QAuthority authority;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final com.aljjabaegi.api.entity.key.QMenuAuthorityKey key;

    public final QMenu menu;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QMenuAuthority(String variable) {
        this(MenuAuthority.class, forVariable(variable), INITS);
    }

    public QMenuAuthority(Path<? extends MenuAuthority> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMenuAuthority(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMenuAuthority(PathMetadata metadata, PathInits inits) {
        this(MenuAuthority.class, metadata, inits);
    }

    public QMenuAuthority(Class<? extends MenuAuthority> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.authority = inits.isInitialized("authority") ? new QAuthority(forProperty("authority")) : null;
        this.key = inits.isInitialized("key") ? new com.aljjabaegi.api.entity.key.QMenuAuthorityKey(forProperty("key")) : null;
        this.menu = inits.isInitialized("menu") ? new QMenu(forProperty("menu")) : null;
    }

}

