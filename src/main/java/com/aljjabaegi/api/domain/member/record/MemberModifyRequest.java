package com.aljjabaegi.api.domain.member.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 사용자 수정 요청 record
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 */
@Schema(description = "사용자 수정 요청 record")
public record MemberModifyRequest(
        @Schema(description = "사용자 ID", example = "honggildong123")
        @NotNull
        String memberId,
        @Schema(description = "사용자 명", example = "홍길동")
        @NotNull
        String memberName,
        @Schema(description = "전화번호", example = "010-1234-5678")
        String cellphone,
        @Schema(description = "사용여부", example = "true")
        boolean isUse
) {
}
