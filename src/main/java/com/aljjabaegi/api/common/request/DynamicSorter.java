package com.aljjabaegi.api.common.request;

import com.aljjabaegi.api.common.request.enumeration.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dynamic sorter
 *
 * @author GEONLEE
 * @since 2024-04-12
 */
@Schema(description = "DynamicSorter")
public record DynamicSorter(
        @Schema(description = "Field to sort by", example = "field")
        String field,
        @Schema(description = "Sort direction [ASC, DESC]", example = "ASC")
        SortDirection direction) {
}
