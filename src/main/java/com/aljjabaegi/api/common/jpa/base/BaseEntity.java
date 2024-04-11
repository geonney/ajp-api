package com.aljjabaegi.api.common.jpa.base;

import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
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
 * 2024-04-05 GEONLEE - createDate updatable = false 추가<br />
 * 2024-04-11 GEONLEE - DynamicSpecification 사용을 위한 @SearchableField 추가<br />
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    @SearchableField
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_dt")
    @SearchableField
    private LocalDateTime modifyDate;
}
