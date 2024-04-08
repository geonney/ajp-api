package com.aljjabaegi.api.domain.team.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 팀 삭제 요청
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Schema(description = "팀 삭제 요청")
public record TeamDeleteRequest(
        @Schema(description = "팀 ID", example = "1")
        Long teamId) {
}
