package com.aljjabaegi.api.common.jpa.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 공통 필드 처리를 위한 Base Entity
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-05 GEONLEE - createDate updatable = false 추가
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_dt")
    private LocalDateTime modifyDate;
}
