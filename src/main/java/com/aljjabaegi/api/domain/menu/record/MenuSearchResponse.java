package com.aljjabaegi.api.domain.menu.record;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Schema(description = "메뉴 응답")
public record MenuSearchResponse(
        @Schema(description = "메뉴 ID", example = "UUID")
        String menuId,
        @Schema(description = "메뉴 명", example = "메뉴명")
        String menuName,
        @Schema(description = "상위 메뉴 ID", example = "UUID")
        String upperMenuId,
        @Schema(description = "메뉴 경로", example = "a/b/c")
        String menuPath,
        @Schema(description = "생성 일시", example = "2024-04-08 15:15:15")
        String createDate,
        @Schema(description = "수정 일시", example = "2024-04-08 15:15:15")
        String modifyDate,
        @Schema(description = "하위 메뉴")
        List<MenuSearchResponse> subMenus
) {
}
