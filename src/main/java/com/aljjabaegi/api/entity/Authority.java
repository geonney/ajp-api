package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * User Entity
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 */
@Getter
@Setter
@Entity(name = "authority")
public class Authority extends BaseEntity {
    @Id
    @Column(name = "authority_cd")
    private String authorityCode;
    @Column(name = "authority_name")
    private String authorityName;

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();
}
