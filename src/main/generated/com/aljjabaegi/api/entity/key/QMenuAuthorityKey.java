package com.aljjabaegi.api.entity.key;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMenuAuthorityKey is a Querydsl query type for MenuAuthorityKey
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMenuAuthorityKey extends BeanPath<MenuAuthorityKey> {

    private static final long serialVersionUID = -1597807374L;

    public static final QMenuAuthorityKey menuAuthorityKey = new QMenuAuthorityKey("menuAuthorityKey");

    public final StringPath authorityCode = createString("authorityCode");

    public final StringPath menuId = createString("menuId");

    public QMenuAuthorityKey(String variable) {
        super(MenuAuthorityKey.class, forVariable(variable));
    }

    public QMenuAuthorityKey(Path<? extends MenuAuthorityKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuAuthorityKey(PathMetadata metadata) {
        super(MenuAuthorityKey.class, metadata);
    }

}

