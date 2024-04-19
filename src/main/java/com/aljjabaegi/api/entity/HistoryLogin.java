package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.entity.key.HistoryLoginKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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

//    @PrePersist
//    public void prePersist() {
//        if (key.getCreateDate() == null) {
//            key.setCreateDate(LocalDateTime.now());
//        }
//    }
}
