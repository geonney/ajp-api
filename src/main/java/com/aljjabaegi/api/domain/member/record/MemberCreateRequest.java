package com.aljjabaegi.api.domain.member.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 사용자 추가 요청 record
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 */
@Schema(description = "사용자 추가 요청 record")
public record MemberCreateRequest(
        @Schema(description = "사용자 ID", example = "honggildong123")
        @NotNull
        String memberId,
        @Schema(description = "사용자 password", example = "c44hzFsJz1TpbuNCc1ZBN5PbSWZI7/SGz1rl80l+ksmvWoQxdTdvjd8s+IP5ihfx+4FoTZLoJ7yIjIuYE8vCroS1SCyDqW7ZiZESMDo5k7VHh03kRbwss+9pdqxfA31qrP3jwmHYGkNgX1sC+/XG5O64M4Mss+Zfs79Z4f8e7ZxqVtWcbgLLGZTe3NMoVCozS3ORzU0YoInzuJujy8PtmeZynw0jWcdQf9x/c/f1N3TV73wARbnmr7ZRqlTCO67JwRlHV5H1Gpata/bGnQxiq5/pjlqxUnkdOH0UT9ZE6IQSNiLprHGYU3TBPysWjTikhpZUmb1SvF031lcFutI7uw==")
        @NotNull
        String password,
        @Schema(description = "사용자 명", example = "홍길동")
        @NotNull
        String memberName,
        @Schema(description = "전화번호", example = "010-1234-5678")
        String cellphone,
        @Schema(description = "사용여부", example = "true")
        boolean isUse,
        @Schema(description = "팀 ID", example = "1")
        @NotNull
        Long teamId,
        @Schema(description = "사용자 권한", hidden = true)
        String authorityCode
) {
}
