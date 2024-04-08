package com.aljjabaegi.api.domain.team.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 팀 생성 응답
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Schema(description = "팀 생성 응답")
public record TeamCreateResponse(

        @Schema(description = "팀 ID", example = "1")
        Long teamId,
        @Schema(description = "팀 명", example = "팀명")
        String teamName,
        @Schema(description = "생성 일시", example = "2024-04-08 15:15:15")
        String createDate,
        @Schema(description = "수정 일시", example = "2024-04-08 15:15:15")
        String modifyDate
) {
}
