package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.annotation.DefaultSort;
import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.jpa.idGenerator.IdGeneratorUtil;
import com.aljjabaegi.api.common.request.enumeration.SortDirection;
import com.aljjabaegi.api.entity.enumerated.UseYn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

import static com.aljjabaegi.api.common.jpa.idGenerator.IdGeneratorUtil.GENERATOR_PARAM_KEY;

/**
 * Project Entity (ID가 특정 형태인 경우 @GenericGenerator)
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 * 2024-04-07 GEONLEE - @Temporal(TemporalType.DATE) 추가<br />
 * 2024-04-24 GEONLEE - DefaultSort 적용<br />
 * 2024-05-24 GEONLEE - isProceeding sub query to case 변경<br />
 */
@Getter
@Setter
@Entity(name = "project")
@DefaultSort(columnName = {"createDate", "projectStartDate"}, direction = {SortDirection.DESC, SortDirection.DESC})
public class Project extends BaseEntity {

    @Id
    @GenericGenerator(name = "ProjectIdGenerator", type = IdGeneratorUtil.class
            , parameters = @org.hibernate.annotations.Parameter(name = GENERATOR_PARAM_KEY, value = "project"))
    @GeneratedValue(generator = "ProjectIdGenerator")
    @Column(name = "project_id")
    private String projectId;

    @Column(name = "project_name")
    @SearchableField
    private String projectName;

    @Column(name = "project_start_date")
    @Temporal(TemporalType.DATE) // 생량 할 경우 date 와 가장 유사한 timestamp 로 정의 됨.
    @SearchableField
    private LocalDate projectStartDate;

    @Column(name = "project_end_date")
    @Temporal(TemporalType.DATE)
    @SearchableField
    private LocalDate projectEndDate;

    @Formula("case when project_start_date >= now() or project_end_date  <= now() then 'Y' else 'N' end")
    @Enumerated(EnumType.STRING)
    @SearchableField
    private UseYn isProceeding;
}
