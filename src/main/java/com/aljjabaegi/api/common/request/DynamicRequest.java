package com.aljjabaegi.api.common.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Dynamic Request with filtering, sorting
 *
 * @author GEONLEE
 * @since 2024-04-12
 */
@Schema(description = "Dynamic Request with filtering, sorting")
public record DynamicRequest(
        @Schema(description = "Current page number", example = "0", defaultValue = "0")
        int pageNo,
        @Schema(description = "Number of data in page", example = "10", defaultValue = "10")
        int pageSize,
        @Schema(description = "Filter array")
        List<DynamicFilter> filter,
        @Schema(description = "Sort array")
        List<DynamicSorter> sorter) {
}
