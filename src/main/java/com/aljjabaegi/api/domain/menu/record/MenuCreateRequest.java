package com.aljjabaegi.api.domain.menu.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Schema(description = "메뉴 응답")
public record MenuCreateRequest(
        @Schema(description = "메뉴 명", example = "메뉴명")
        String menuName,
        @Schema(description = "상위 메뉴 ID", example = "null")
        String upperMenuId,
        @Schema(description = "메뉴 경로", example = "a/b/c")
        String menuPath
) {
}
