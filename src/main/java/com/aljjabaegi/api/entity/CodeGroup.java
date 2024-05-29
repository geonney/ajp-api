package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-05-29
 */
@Getter
@Setter
@Entity(name = "code_group")
public class CodeGroup extends BaseEntity {
    @Id
    @Column(name = "code_group_id")
    String codeGroupId;

    @Column(name = "code_group_nm")
    String codeGroupName;

    @Column(name = "code_group_desc")
    String codeGroupDescription;

    @OneToMany(mappedBy = "codeGroup")
    List<Code> codes = new ArrayList<>();
}
