package com.aljjabaegi.api.domain.board.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시판 추가 응답 record
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 */
@Schema(description = "게시판 추가 응답 record")
public record BoardCreateResponse(
        @Schema(description = "게시판 시퀀스", example = "1")
        Long boardSequence,
        @Schema(description = "게시판 제목", example = "제목")
        String boardTitle,
        @Schema(description = "게시판 내용", example = "내용")
        String boardContent,
        @Schema(description = "생성 일시", example = "2024-04-01 00:00:00")
        String createDate,
        @Schema(description = "수정 일시", example = "2024-04-01 00:00:00")
        String modifyDate
) {
}
