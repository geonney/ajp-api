package com.aljjabaegi.api.common.validator;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Custom ConstraintValidator implementation<br />
 * DynamicRequest filter 에 대한 필수 컬럼 처리
 *
 * @author GEONLEE
 * @since 2024-06-26
 */
public class DynamicValidator implements ConstraintValidator<DynamicValid, DynamicRequest> {

    private List<String> essentialFields;

    @Override
    public void initialize(DynamicValid constraintAnnotation) {
        this.essentialFields = List.of(constraintAnnotation.essentialFields());
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DynamicRequest dynamicRequest, ConstraintValidatorContext context) {
        //Filter only non-null values
        List<String> filterFields = dynamicRequest.filter().stream()
                .filter(dynamicFilter -> !ObjectUtils.isEmpty(dynamicFilter.value()))
                .map(DynamicFilter::field).toList();
        //EssentialField check
        List<String> invalidFields = essentialFields.stream()
                .filter(essentialField -> !filterFields.contains(
                        (essentialField.contains(":") ? essentialField.split(":")[0].trim() : essentialField.trim())))
                .toList();
        if (invalidFields.size() > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(CommonErrorCode.REQUIRED_PARAMETER.message())
                    .addPropertyNode(String.join(", ", invalidFields))
                    .addConstraintViolation();
        }
        return invalidFields.size() == 0;
    }
}
