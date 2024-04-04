package com.aljjabaegi.api.domain.project.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Project 수정 요청 record
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 */
@Schema(description = "Project 수정 요청 record")
public record ProjectModifyRequest(
        @Schema(description = "프로젝트 ID", example = "PJ001")
        @NotNull
        String projectId,
        @Schema(description = "프로젝트 명", example = "프로젝트1")
        @NotNull
        String projectName,
        @Schema(description = "프로젝트 시작일자", example = "2024-04-04")
        String projectStartDate,
        @Schema(description = "프로젝트 종료일자", example = "2024-04-04")
        String projectEndDate
) {
}
