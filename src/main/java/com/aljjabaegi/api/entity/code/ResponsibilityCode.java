package com.aljjabaegi.api.entity.code;

import com.aljjabaegi.api.entity.key.CodeKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author GEONLEE
 * @since 2024-06-04
 */
@Entity
@Getter
@Setter
@Table(name = "code")
public class ResponsibilityCode {

    @EmbeddedId
    private CodeKey codeKey;

    @Column(name = "code_nm")
    private String codeName;
}
