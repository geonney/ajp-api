package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.jpa.idGenerator.IdGeneratorUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

import static com.aljjabaegi.api.common.jpa.idGenerator.IdGeneratorUtil.GENERATOR_PARAM_KEY;

/**
 * Project Entity (ID가 특정 형태인 경우 @GenericGenerator)
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Getter
@Setter
@Entity(name = "project")
public class Project extends BaseEntity {

    @Id
    @GenericGenerator(name = "ProjectIdGenerator", type = IdGeneratorUtil.class
            , parameters = @org.hibernate.annotations.Parameter(name = GENERATOR_PARAM_KEY, value = "project"))
    @GeneratedValue(generator = "ProjectIdGenerator")
    @Column(name = "project_id")
    private String projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_start_date")
    private LocalDate projectStartDate;

    @Column(name = "project_end_date")
    private LocalDate projectEndDate;
}
