package com.aljjabaegi.api.domain.login.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 로그아웃 응답
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Builder
@Schema(description = "로그아웃 응답")
public record LogoutResponse(
        @Schema(description = "응답 코드", example = "OK")
        String status,
        @Schema(description = "응답 메시지", example = "로그아웃 하였습니다.")
        String message) {
}
