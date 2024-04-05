package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * User Entity (ID를 입력 전달받는 경우)
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-02 GEONLEE - DB migration 으로 테이블 명칭 변경<br />
 */
@Getter
@Setter
@Entity(name = "member")
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_name")
    private String memberName;

    private String cellphone;

    @Column(name = "use_yn")
    private String useYn;

    private String accessToken;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_cd")
    @NotFound(action = NotFoundAction.IGNORE)
    private Authority authority;

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
