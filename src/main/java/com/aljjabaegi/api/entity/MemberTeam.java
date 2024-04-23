package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GEONLEE
 * @since 2024-04-23
 */
@Getter
@Setter
@ToString
@Entity(name = "member_team")
public class MemberTeam extends BaseEntity {

    @Id
    @Column(name = "memberId")
    private String memberId;

    @Column(name = "rpbt_cd")
    private String responsibilitiesCode;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
