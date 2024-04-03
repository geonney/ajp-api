package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * User Entity
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
    @Column(name = "user_id")
    private String memberId;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name")
    private String memberName;

    private String cellphone;

    @Column(name = "use_yn")
    private String useYn;

    private String accessToken;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_cd")
    private Authority authority;
}
