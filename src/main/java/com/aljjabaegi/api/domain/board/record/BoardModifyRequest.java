package com.aljjabaegi.api.domain.board.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 게시판 수정 요청 record
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 */
@Schema(description = "게시판 수정 요청 record")
public record BoardModifyRequest(
        @Schema(description = "게시판 시퀀스", example = "1")
        Long boardSequence,
        @Schema(description = "게시판 제목", example = "제목")
        @NotNull
        String boardTitle,
        @Schema(description = "게시판 내용", example = "내용")
        @NotNull
        String boardContent
) {
}
