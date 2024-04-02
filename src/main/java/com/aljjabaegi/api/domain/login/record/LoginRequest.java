package com.aljjabaegi.api.domain.login.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 로그인 요청 record
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
@Schema(description = "로그인 요청 record")
public record LoginRequest(
        @Schema(description = "사용자 ID", example = "honggildong123")
        @NotNull
        String id,
        @Schema(description = "패스워드", example = "1234")
        @NotNull
        String password
) {

}
