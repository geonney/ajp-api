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
        @Schema(description = "사용자 password", example = "XO4y9+pNfqOEpaK3SYbf1n/EbsWiAVgv7arisRDKbmB0xcyx0bd+922YgEc/5B/ZnbKXD/oXkVWh3BMFAiu2Vqf97XEbOdWwpbvGNG4vrxoVD6NPDsv9PZRHHwjTIxpOIiIKGWqqAKKtMUJYcv7UUJTidHAe4IxvaqIlleWQLLVsm3xd/72XXjkdCDmFvOXpTkL01KLeeL0qzkBGys8eEuNQC/8U+qg5ocg6RRIUTCnLuyX6yO79XLlQUSuyA7L9Vz872nEVy2JJeSxqko32GKLg3sy+xDP9YUpFroaeKXXdNnsNj6hsOuXRla7HxgYSHMElOxujlN0lvBSaeBt6gg==")
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
