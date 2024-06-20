package com.aljjabaegi.api.domain.member.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 사용자 조회 응답 record
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Schema(description = "사용자 조회 응답 record")
public record MemberSearchResponse(
        @Schema(description = "사용자 ID", example = "honggildong123")
        String memberId,
        @Schema(description = "사용자 명", example = "홍길동")
        String memberName,
        @Schema(description = "생년월일", example = "1980-01-01")
        String birthDate,
        @Schema(description = "전화번호", example = "010-1234-5678")
        String cellphone,
        @Schema(description = "팀 명", example = "팀 명")
        String teamName,
        @Schema(description = "직책", example = "팀원")
        String responsibilityCodeName,
        @Schema(description = "생성 일시", example = "2024-04-01 00:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createDate,
        @Schema(description = "수정 일시", example = "2024-04-01 00:00:00")
        String modifyDate
) {
}
