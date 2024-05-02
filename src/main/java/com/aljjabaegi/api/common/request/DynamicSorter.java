package com.aljjabaegi.api.common.request;

import com.aljjabaegi.api.common.request.enumeration.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Dynamic sorter
 *
 * @author GEONLEE
 * @since 2024-04-12<br />
 * 2024-05-02 GEONLEE - @Builder 추가<br />
 */
@Schema(description = "DynamicSorter")
@Builder
public record DynamicSorter(
        @Schema(description = "Field to sort by", example = "field")
        String field,
        @Schema(description = "Sort direction [ASC, DESC]", example = "ASC")
        SortDirection direction) {
}
