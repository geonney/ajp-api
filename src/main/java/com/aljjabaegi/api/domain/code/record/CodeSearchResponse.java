package com.aljjabaegi.api.domain.code.record;

import com.aljjabaegi.api.entity.enumerated.UseYn;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 코드 조회 응답
 *
 * @author GEONLEE
 * @since 2024-05-29
 */
@Schema(description = "코드 조회 응답")
public record CodeSearchResponse(
        @Schema(description = "코드 그룹 ID", example = "rpbt_cd")
        String codeGroupId,
        @Schema(description = "코드 ID", example = "cd01")
        String codeId,
        @Schema(description = "코드 명", example = "코드명")
        String codeName,
        @Schema(description = "코드 순번", example = "1")
        Integer codeOrder,
        @Schema(description = "사용 여부", example = "Y")
        UseYn useYn,
        @Schema(description = "생성 일시", example = "2024-05-29 00:00:00")
        String createDate,
        @Schema(description = "수정 일시", example = "2024-05-29 00:00:00")
        String modifyDate

) {
}
