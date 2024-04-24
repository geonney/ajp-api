package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.annotation.DefaultSort;
import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.request.enumeration.SortDirections;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * Board Entity (ID가 시퀀스인 경우)
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 * 2024-04-19 GEONLEE - Member 관계 추가<br />
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
@DefaultSort(columnName = {"boardSequence"}, direction = SortDirections.DESC)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "board_seq")
    @SearchableField
    private Long boardSequence;

    @Column(name = "board_title")
    @SearchableField
    private String boardTitle;

    @Column(name = "board_content")
    @SearchableField
    private String boardContent;

    @Column(name = "view_cnt")
    @SearchableField
    private int viewCount;

    @SearchableField
    @LastModifiedBy
    @Column(name = "modify_member_id")
    private String memberId;

    @JoinColumn(name = "modify_member_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne 단방향
    private Member member;
}
