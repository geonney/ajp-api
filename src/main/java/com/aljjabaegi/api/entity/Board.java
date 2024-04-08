package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * Board Entity (ID가 시퀀스인 경우)
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Getter
@Setter
@Entity(name = "board")
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR"
        , sequenceName = "board_seq"
        , initialValue = 1
        , allocationSize = 1
)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "board_seq")
    private Long boardSequence;

    @Column(name = "board_title")
    private String boardTitle;

    @Column(name = "board_content")
    private String boardContent;

    @Column(name = "modify_member_id")
    @LastModifiedBy
    private String modifyMemberId;
}
