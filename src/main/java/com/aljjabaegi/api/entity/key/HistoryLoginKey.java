package com.aljjabaegi.api.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 로그인 이력 키
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class HistoryLoginKey implements Serializable {

    @Column(name = "create_dt", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "member_id")
    private String memberId;
}
