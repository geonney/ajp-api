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
 * 2024-04-26 GEONLEE - loginAttemptsCont 로그인 시도 횟수 컬럼 추가<br />
 * 2024-07-22 GEONLEE - NamedEntityGraph 추가<br />
 */
@Getter
@Setter
@Entity(name = "member")
@NamedEntityGraph(
        name = "Member_GRAPH",
        attributeNodes = {
                @NamedAttributeNode(value = "memberTeam", subgraph = "memberTeam"),
                @NamedAttributeNode(value = "authority"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "memberTeam",
                        attributeNodes = {
                                @NamedAttributeNode("responsibilityCode"),
                                @NamedAttributeNode("team")
                        }
                )
        }
)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @SearchableField
    private String memberId;

    @Column(name = "member_pw")
    private String password;

    @Transient // 영속성 관리 대상에서 제외하는 속성, 내부 로직에 관리가 필요한 경우 추가해서 사용한다.
    private String confirmPassword;

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
    @SearchableField
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

    @Column(name = "login_attempts")
    private Integer loginAttemptsCount;

    @ManyToOne(fetch = FetchType.EAGER) //ManyToOne - OneToMany 양방향
    @JoinColumn(name = "authority_cd")
    @NotFound(action = NotFoundAction.IGNORE)
    private Authority authority;

//    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne - OneToMany 양방향
//    @JoinColumn(name = "team_id")
//    @SearchableField(columnPath = "team.teamName")
//    private Team team = new Team(); // team 이 필수인 경우 = new Team() 으로 초기화. 저장 시 Team 이 없으면 Exception  발생

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    @SearchableField(columnPath = "memberTeam.team.teamName")
    private MemberTeam memberTeam = new MemberTeam();

    @Transient /* 컬럼으로 관리할 필요가 없는 필드인 경우 사용 (영속성 대상에서 제외, querydsl Qentity 대성에서도 제외)*/
    private UseYn lockYn;

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
}
