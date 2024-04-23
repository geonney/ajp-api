package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.entity.key.MenuAuthorityKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Getter
@Setter
@Entity(name = "menu_authority")
public class MenuAuthority extends BaseEntity {

    @EmbeddedId
    @SearchableField(columnPath = "key.authorityCode")
    private MenuAuthorityKey key;

    //단순 참조 연결
    @ManyToOne
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "authority_code", insertable = false, updatable = false)
    private Authority authority;
}
