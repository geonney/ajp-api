package com.aljjabaegi.api.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 공통 오류 응답 구조체
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Schema(description = "공통 오류 응답 구조체")
@Builder
public record ErrorResponse (
        @Schema(description = "상태 코드", example = "ER_SV_01")
        String status,
        @Schema(description = "메시지", example = "요청한 서비스에 문제가 발생했습니다. 잠시 후에 다시 시도해 주세요.")
        String message
) {
}
