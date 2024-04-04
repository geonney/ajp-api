package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Project Entity (ID가 시퀀스인 경우)
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Getter
@Setter
@Entity(name = "project")
@SequenceGenerator(
        name = "PROJECT_SEQ_GENERATOR"
        , sequenceName = "project_project_seq_seq"
        , initialValue = 1
        , allocationSize = 1
)
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_SEQ_GENERATOR")
    @Column(name = "project_seq")
    private Long projectSequence;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_start_date")
    private LocalDate projectStartDate;

    @Column(name = "project_end_date")
    private LocalDate projectEndDate;
}
