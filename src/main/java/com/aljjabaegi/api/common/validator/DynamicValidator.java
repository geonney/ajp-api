package com.aljjabaegi.api.common.validator;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Custom ConstraintValidator implementation<br />
 * DynamicRequest filter 에 대한 필수 컬럼 처리
 *
 * @author GEONLEE
 * @since 2024-06-26
 */
public class DynamicValidator implements ConstraintValidator<DynamicValid, DynamicRequest> {

    private List<String> essentialFields;
    private List<FieldValid> fieldValidations;

    @Override
    public void initialize(DynamicValid constraintAnnotation) {
        this.essentialFields = List.of(constraintAnnotation.essentialFields());
        this.fieldValidations = List.of(constraintAnnotation.fieldValidations());
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DynamicRequest dynamicRequest, ConstraintValidatorContext context) {
        //Filter only non-null values
        Map<String, String> filterFields = dynamicRequest.filter().stream()
                .filter(dynamicFilter -> ObjectUtils.isNotEmpty(dynamicFilter.value()))
                .collect(Collectors.toMap(DynamicFilter::field, DynamicFilter::value));
        //EssentialField check
        List<String> invalidFields = this.essentialFields.stream()
                .filter(essentialField -> !filterFields.containsKey(
                        (essentialField.contains(":") ? essentialField.split(":")[0].trim() : essentialField.trim())))
                .toList();
        //Validation check
        System.out.println(filterFields.keySet().toString());
//        List<FieldValid> inValidField2 = this.fieldValidations.stream()
//                .filter(fieldValid -> !filterFields.containsKey(fieldValid.fieldName()) &&
//                        !Pattern.compile(fieldValid.pattern().format()).matcher(filterFields.get(fieldValid.fieldName())).matches())
//                .toList();
//        System.out.println(inValidField2);

        if (invalidFields.size() > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(CommonErrorCode.REQUIRED_PARAMETER.message())
                    .addPropertyNode(String.join(", ", invalidFields))
                    .addConstraintViolation();
        }
        return invalidFields.size() == 0;
    }
}
