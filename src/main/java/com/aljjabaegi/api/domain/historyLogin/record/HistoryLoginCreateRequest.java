package com.aljjabaegi.api.domain.historyLogin.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인 이력 저장 record
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Schema(description = "로그인 이력 저장 record")
public record HistoryLoginRequest(

        @Schema(description = "생성 일시")
        String createDate,

        @Schema(description = "사용자 ID")
        String memberId,

        @Schema(description = "로그인 IP")
        String loginIp
) {
}
