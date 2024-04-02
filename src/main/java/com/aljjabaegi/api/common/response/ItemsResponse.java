package com.aljjabaegi.api.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 공통 List 응답 구조체
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Schema(description = "공통 List 응답 구조체")
@Builder
public record ItemsResponse<T>(
        @Schema(description = "상태 코드")
        String status,
        @Schema(description = "메시지")
        String message,
        @Schema(description = "List 응답 객체 size")
        Long size,
        @Schema(description = "List 응답 객체")
        List<T> items
) {
}
