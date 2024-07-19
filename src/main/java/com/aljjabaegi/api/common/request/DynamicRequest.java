package com.aljjabaegi.api.common.request;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.request.enumeration.Operator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Dynamic Request with filtering, sorting
 *
 * @author GEONLEE
 * @since 2024-04-12<br />
 * 2024-05-02 GEONLEE - @Builder 추가<br />
 */
@Schema(description = "Dynamic Request with filtering, sorting")
@Builder
public record DynamicRequest(
        @Schema(description = "Current page number", example = "0", defaultValue = "0")
        int pageNo,
        @Schema(description = "Number of data in page", example = "10", defaultValue = "10")
        int pageSize,
        @Schema(description = "Filter array")
        List<DynamicFilter> filter,
        @Schema(description = "Sort array")
        List<DynamicSorter> sorter) {
    public DynamicRequest {
        pageSize = (pageSize == 0) ? 10 : pageSize;
    }

    /**
     * filter 에서 field name 의 값을 추출할 떄 사용<br />
     * native query 를 사용 시 filter value 를 추출할 떄 사용한다.
     *
     * @return String
     * @author GEONLEE
     * @since 2024-07-15
     */
    public String getFieldValue(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "'fieldName' can't be empty.");
        }
        return this.filter.stream()
                .filter(filter -> fieldName.equals(filter.field()))
                .map(DynamicFilter::value)
                .findFirst()
                .orElseThrow(() -> new ServiceException(CommonErrorCode.INVALID_PARAMETER, fieldName + " value doesn't exist."));
    }

    /**
     * filter 에서 field name, Operator 가 같은 값을 추출할 떄 사용<br />
     * native query 를 사용 시 filter value 를 추출할 떄 사용한다.
     *
     * @author GEONLEE
     * @since 2024-07-15
     */
    public String getFieldValue(String fieldName, Operator operator) {
        if (StringUtils.isEmpty(fieldName)) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "'fieldName' can't be empty.");
        }
        return this.filter.stream()
                .filter(dynamicFilter -> fieldName.equals(dynamicFilter.field()) && dynamicFilter.operator() == operator)
                .map(DynamicFilter::value)
                .findFirst()
                .orElseThrow(() -> new ServiceException(CommonErrorCode.INVALID_PARAMETER, fieldName + " value doesn't exist."));
    }

    /**
     * filter 에서 field name 의 값을 추출할 떄 사용<br />
     * native query 를 사용 시 filter value 를 추출할 떄 사용한다.
     *
     * @return Optional<String>
     * @author GEONLEE
     * @since 2024-07-19
     */
    public Optional<String> getFieldValueAsOptional(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "'fieldName' can't be empty.");
        }
        return this.filter.stream()
                .filter(filter -> fieldName.equals(filter.field()))
                .map(DynamicFilter::value)
                .findFirst();
    }
}
