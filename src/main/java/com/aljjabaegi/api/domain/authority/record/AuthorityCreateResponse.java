package com.aljjabaegi.api.domain.authority.record;

import com.aljjabaegi.api.domain.menu.record.MenuSimpleResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-04-22
 */
@Schema(description = "메뉴 생성 응답")
public record AuthorityCreateResponse(
        @Schema(description = "권한 코드")
        String authorityCode,
        @Schema(description = "권한 명")
        String authorityName,
        @Schema(description = "메뉴 리스트")
        List<MenuSimpleResponse> menus,
        @Schema(description = "생성 일시", example = "2024-04-08 15:15:15")
        String createDate,
        @Schema(description = "수정 일시", example = "2024-04-08 15:15:15")
        String modifyDate
) {
}
