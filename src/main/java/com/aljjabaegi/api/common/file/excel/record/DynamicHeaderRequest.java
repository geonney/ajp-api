package kr.co.neighbor21.neighborApi.common.util.file.excel.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import lombok.Builder;

/**
 * Excel download 시 동적 헤더 설정 시 사용
 *
 * @author GEONLEE
 * @since 2024-07-08
 */
@Builder(builderMethodName = "innerBuilder")
public record DynamicHeaderRequest(
        @Schema(description = "[필수] Header 한글 명", example = "헤더1")
        @Nonnull
        String headerName,
        @Schema(description = "[필수] Header column name")
        @Nonnull
        String columnName,
        @Schema(description = "cell 의 넓이", example = "200", defaultValue = "200")
        int cellWidth

) {
    public static DynamicHeaderRequest.DynamicHeaderRequestBuilder builder(String headerName, String columnName) {
        return innerBuilder().headerName(headerName).columnName(columnName);
    }
}
