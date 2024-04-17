package com.aljjabaegi.api.config.security.jwt.record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * API 사용자에게 전달되는 Response token 구조체
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 * 2024-04-17 GEONLEE - isChangePassword 추가<br />
 */
@Schema(description = "JWT Token 응답 DTO")
@Builder
public record TokenResponse(
        @Schema(description = "Access Token", example = "eyJhbGciOiJSUzI1NiJ9...", hidden = true)
        @JsonIgnore
        String token,
        @Schema(description = "Refresh Token", example = "eyJhbGciOiJSUzI1NiJ9...")
        String refreshToken,
        @Schema(description = "Token 타입", example = "Bearer")
        String tokenType,
        @Schema(description = "Token 만료 시간 (초)", example = "600000")
        Long expirationSeconds,
        @Schema(description = "패스워드 변경 여부", example = "false")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isChangePassword) {
}