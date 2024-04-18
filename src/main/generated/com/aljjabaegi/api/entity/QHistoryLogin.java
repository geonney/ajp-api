package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHistoryLogin is a Querydsl query type for HistoryLogin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHistoryLogin extends EntityPathBase<HistoryLogin> {

    private static final long serialVersionUID = -232759971L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHistoryLogin historyLogin = new QHistoryLogin("historyLogin");

    public final com.aljjabaegi.api.entity.key.QHistoryLoginKey key;

    public final StringPath loginIp = createString("loginIp");

    public QHistoryLogin(String variable) {
        this(HistoryLogin.class, forVariable(variable), INITS);
    }

    public QHistoryLogin(Path<? extends HistoryLogin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHistoryLogin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHistoryLogin(PathMetadata metadata, PathInits inits) {
        this(HistoryLogin.class, metadata, inits);
    }

    public QHistoryLogin(Class<? extends HistoryLogin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.key = inits.isInitialized("key") ? new com.aljjabaegi.api.entity.key.QHistoryLoginKey(forProperty("key")) : null;
    }

}

