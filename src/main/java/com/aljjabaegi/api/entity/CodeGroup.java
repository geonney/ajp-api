package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-05-29<br />
 * 2024-07-03 GEONLEE - OrderBy annotation 추가<br />
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
    @OrderBy("key.codeId ASC")
    List<Code> codes = new ArrayList<>();
}
