package com.aljjabaegi.api.domain.team.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 팀 수정 요청
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Schema(description = "팀 수정 요청")
public record TeamModifyRequest(
        @Schema(description = "팀 ID", example = "1")
        Long teamId,
        @Schema(description = "팀 명", example = "팀명")
        String teamName) {
}
