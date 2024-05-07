package com.aljjabaegi.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -1706193935L;

    public static final QProject project = new QProject("project");

    public final com.aljjabaegi.api.common.jpa.base.QBaseEntity _super = new com.aljjabaegi.api.common.jpa.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final EnumPath<com.aljjabaegi.api.entity.enumerated.UseYn> isProceeding = createEnum("isProceeding", com.aljjabaegi.api.entity.enumerated.UseYn.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final DatePath<java.time.LocalDate> projectEndDate = createDate("projectEndDate", java.time.LocalDate.class);

    public final StringPath projectId = createString("projectId");

    public final StringPath projectName = createString("projectName");

    public final DatePath<java.time.LocalDate> projectStartDate = createDate("projectStartDate", java.time.LocalDate.class);

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}

