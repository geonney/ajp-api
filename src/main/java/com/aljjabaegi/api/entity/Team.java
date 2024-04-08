package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-04-08
 */
@Getter
@Setter
@Entity(name = "team")
@SequenceGenerator(
        name = "TEAM_SEQ_GENERATOR"
        , sequenceName = "team_seq"
        , initialValue = 1
        , allocationSize = 1
)
public class Team extends BaseEntity {

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    List<Member> members = new ArrayList<>();

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEAM_SEQ_GENERATOR")
    private Long teamId;

    @Column(name = "team_name")
    private String teamName;
}
