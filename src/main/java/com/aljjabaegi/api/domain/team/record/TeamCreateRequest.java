package com.aljjabaegi.api.domain.team.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 팀 생성 요청
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Schema(description = "팀 생성 요청")
public record TeamCreateRequest(
        @Schema(description = "팀 명", example = "팀명")
        String teamName) {
}
