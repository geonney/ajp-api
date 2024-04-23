package com.aljjabaegi.api.domain.authority.record;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Schema(description = "권한 생성 요청")
public record AuthorityCreateRequest(
        @Schema(description = "권한 코드", example = "ROLE_MANAGER")
        String authorityCode,
        @Schema(description = "권한 명", example = "매니저 권한")
        String authorityName,
        @Schema(description = "권한이 갖게 될 메뉴 ID 리스트")
        List<String> menuIds
) {
}
