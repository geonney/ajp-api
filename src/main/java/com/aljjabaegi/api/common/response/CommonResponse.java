package com.aljjabaegi.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 공통 응답 구조체
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Schema(description = "공통 응답 구조체")
public record CommonResponse<T>(
        @Schema(description = "상태 코드")
        String status,
        @Schema(description = "메시지")
        String message,
        @Schema(description = "응답 객체")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T item
) {
}
