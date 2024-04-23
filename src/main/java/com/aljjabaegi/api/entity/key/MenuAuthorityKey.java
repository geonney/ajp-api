package com.aljjabaegi.api.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class MenuAuthorityKey implements Serializable {
    @Column(name = "menu_id")
    private String menuId;
    @Column(name = "authority_code")
    private String authorityCode;
}
