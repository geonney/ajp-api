package com.aljjabaegi.api.domain.menu.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Schema(description = "심플 메뉴 응답")
public record MenuSimpleResponse(
        @Schema(description = "메뉴 ID", example = "UUID")
        String menuId,
        @Schema(description = "메뉴 명", example = "메뉴")
        String menuName
) {
}
