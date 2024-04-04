package com.aljjabaegi.api.domain.project.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Project 조회 응답 record
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Schema(description = "Project 조회 응답 record")
public record ProjectSearchResponse(
        @Schema(description = "프로젝트 ID", example = "PJ001")
        String projectId,
        @Schema(description = "프로젝트 명", example = "프로젝트1")
        String projectName,
        @Schema(description = "프로젝트 시작일자", example = "2024-04-04")
        String projectStartDate,
        @Schema(description = "프로젝트 종료일자", example = "2024-12-31")
        String projectEndDate,
        @Schema(description = "생성 일시", example = "2024-04-01 00:00:00")
        String createDate,
        @Schema(description = "수정 일시", example = "2024-04-01 00:00:00")
        String modifyDate
) {
}
