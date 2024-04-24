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
@Schema(description = "공통 Grid List 응답 구조체 (Paging and sorting)")
@Builder
public record GridResponse<T>(
        @Schema(description = "Status code", example = "OK")
        String status,
        @Schema(description = "message", example = "데이터를 조회하는데 성공하였습니다.")
        String message,
        @Schema(description = "Total data size", example = "1")
        Long totalSize,
        @Schema(description = "total page size", example = "1")
        int totalPageSize,
        @Schema(description = "number of element size", example = "1")
        int size,
        @Schema(description = "contents")
        List<T> items
) {
}
