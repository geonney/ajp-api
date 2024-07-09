package com.aljjabaegi.api.common.file.excel.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.util.List;

/**
 * Excel download 요청<br />
 *
 * @author GEONLEE
 * @since 2024-07-05<br />
 * 2024-07-08 GEONLEE - dynamicHeader 추가<br />
 */
@Builder(builderMethodName = "innerBuilder")
public record ExcelDownloadRequest(
        @Schema(description = "[필수]데이터의 record type", example = "_Response.class")
        @Nonnull
        Class<?> recordType,
        @Schema(description = "[필수]엑셀 파일에 추가할 데이터")
        @Nonnull
        List<?> data,
        @Schema(description = "다운로드 할 파일 명 (확장자, 일시 제외)", example = "Excel", defaultValue = "Excel")
        String fileName,
        @Schema(description = "resource/template 경로의 파일을 사용할 경우 해당 파일 명(확장자 제외)", example = "busStop")
        String templateFileName,
        @Schema(description = "동적 헤더 설정 시 사용")
        List<DynamicHeaderRequest> dynamicHeader

) {
    public static ExcelDownloadRequestBuilder builder(Class<?> recordType, List<?> data) {
        return innerBuilder().recordType(recordType).data(data);
    }
}
