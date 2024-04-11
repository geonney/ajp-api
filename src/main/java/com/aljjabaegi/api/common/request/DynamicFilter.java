package com.aljjabaegi.api.common.request;

import com.aljjabaegi.api.common.request.enumeration.Operators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Dynamic filter
 *
 * @author GEONLEE
 * @since 2024-04-09
 */
@Schema(description = "DynamicFilter")
@Builder
public record DynamicFilter(
        @Schema(description = "Field for Search", example = "field")
        String field,
        @Schema(description = "Search operator [eq, contains, between, in]", example = "eq")
        Operators operator,
        @Schema(description = "Value for search", example = "value")
        String value) {
}
