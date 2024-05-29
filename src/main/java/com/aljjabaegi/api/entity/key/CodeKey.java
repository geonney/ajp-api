package com.aljjabaegi.api.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * @author GEONLEE
 * @since 2024-05-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CodeKey implements Serializable {
    @Column(name = "code_group_id")
    private String codeGroupId;
    @Column(name = "code_id")
    private String codeId;
}
