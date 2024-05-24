package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.entity.key.HistoryLoginKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 이력 Entity
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Getter
@Setter
@Entity(name = "history_login")
public class HistoryLogin {

    @EmbeddedId
    private HistoryLoginKey key;

    @Column(name = "login_ip")
    private String loginIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    @MapsId("memberId")
    private Member member;

//    @PrePersist
//    public void prePersist() {
//        if (key.getCreateDate() == null) {
//            key.setCreateDate(LocalDateTime.now());
//        }
//    }
}
