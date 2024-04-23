package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.entity.enumerated.UseYn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDate;

/**
 * User Entity (ID를 입력 전달받는 경우)
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-02 GEONLEE - DB migration 으로 테이블 명칭 변경<br />
 * 2024-04-07 GEONLEE - @Enumerated(EnumType.STRING) 테스트<br />
 * 2024-04-11 GEONLEE - DynamicSpecification 사용을 위한 @SearchableField 추가<br />
 * 2024-04-17 GEONLEE - passwordUpdateDate 추가<br />
 */
@Getter
@Setter
@Entity(name = "member")
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @SearchableField
    private String memberId;

    @Column(name = "member_pw")
    private String password;

    @Column(name = "member_nm")
    @SearchableField
    private String memberName;

    @SearchableField
    private String cellphone;

    @Column(name = "birth_dt")
    @SearchableField
    private LocalDate birthDate;

    @Column(name = "age")
    @SearchableField
    private Integer age;

    @Column(name = "height")
    private Double height;

    @Column(name = "use_yn")
    @Enumerated(EnumType.STRING)
    @SearchableField
    private UseYn useYn;

    @Column(name = "atk")
    private String accessToken;

    @Column(name = "rtk")
    private String refreshToken;

    @Column(name = "pw_update_dt")
    private LocalDate passwordUpdateDate;

    @ManyToOne(fetch = FetchType.EAGER) //ManyToOne - OneToMany 양방향
    @JoinColumn(name = "authority_cd")
    @NotFound(action = NotFoundAction.IGNORE)
    private Authority authority;

    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne - OneToMany 양방향
    @JoinColumn(name = "team_id")
    @SearchableField(columnPath = "team.teamName")
    private Team team = new Team(); // team 이 필수인 경우 = new Team() 으로 초기화. 저장 시 Team 이 없으면 Exception  발생

    // 연관관계 편의 메서드
    public void setAuthority(Authority authority) {
        // 기존 팀과 연관관계를 제거
        if (this.authority != null) {
            this.authority.getMembers().remove(this);
        }

        //새로운 연관관계 설정
        this.authority = authority;
        if (authority != null) {
            authority.getMembers().add(this);
        }
    }

    // 연관관계 편의 메서드
    public void setTeam(Team team) {
        // 기존 팀과 연관관계를 제거
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }

        //새로운 연관관계 설정
        this.team = team;
        if (team != null) {
            team.getMembers().add(this);
        }
    }
}
