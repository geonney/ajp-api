package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.entity.code.ResponsibilityCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

/**
 * @author GEONLEE
 * @since 2024-04-23
 */
@Getter
@Setter
@Entity(name = "member_team")
public class MemberTeam extends BaseEntity {

    @Id
    @Column(name = "memberId")
    private String memberId;

    @Column(name = "rpbt_cd")
    private String responsibilityCodeId;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "rpbt_cd", referencedColumnName = "code_id", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'rpbt_cd'", referencedColumnName = "code_group_id"))
    })
    private ResponsibilityCode responsibilityCode;

    @OneToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
