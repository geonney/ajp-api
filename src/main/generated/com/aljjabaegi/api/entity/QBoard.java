package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -1512012354L;

    public static final QBoard board = new QBoard("board");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    public final StringPath boardContent = createString("boardContent");

    public final NumberPath<Long> boardSequence = createNumber("boardSequence", Long.class);

    public final StringPath boardTitle = createString("boardTitle");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath modifyMemberId = createString("modifyMemberId");

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

