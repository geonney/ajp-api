package com.aljjabaegi.api.domain.historyLogin.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 로그인 이력 조회 record
 *
 * @author GEONLEE
 * @since 2024-04-09
 */
@Schema(description = "로그인 이력 조회 record")
@Builder
public record HistoryLoginSearchResponse(

        @Schema(description = "생성 일시", example = "2024-04-09 00:00:00")
        String createDate,

        @Schema(description = "사용자 ID", example = "honggildong123")
        String memberId,

        @Schema(description = "로그인 IP", example = "127.0.0.1")
        String loginIp
) {
}
