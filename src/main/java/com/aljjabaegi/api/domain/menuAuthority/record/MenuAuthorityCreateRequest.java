package com.aljjabaegi.api.domain.menuAuthority.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Schema(description = "메뉴 권한 생성 요청")
@Builder
public record MenuAuthorityCreateRequest(
        @Schema(description = "메뉴 ID", example = "UUID")
        String menuId,
        @Schema(description = "권한 코드", example = "ROLE_MANAGER")
        String authorityCode
) {
}
