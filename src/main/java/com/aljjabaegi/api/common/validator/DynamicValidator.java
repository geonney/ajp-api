package com.aljjabaegi.api.common.validator;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom ConstraintValidator implementation<br />
 * DynamicRequest filter 에 대한 필수 컬럼 처리<br />
 *
 * @author GEONLEE
 * @since 2024-06-26<br />
 * 2024-07-10 GEONLEE - @FieldValid 를 사용한 유효성 체크 로직 추가<br />
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
        List<String> notPresentList = this.essentialFields.stream()
                .filter(essentialField -> !filterFields.containsKey(getFieldName(essentialField, 0)))
                .map(essentialField -> (essentialField.contains(":")) ? essentialField.split(":")[1].trim() + " 은(는) 필수 입니다." : essentialField)
                .toList();
        //Validation check
        List<String> inValidFieldList = this.fieldValidations.stream()
                .filter(fieldValid -> filterFields.containsKey(getFieldName(fieldValid.fieldName(), 0)))
                .filter(fieldValid -> !Pattern.compile(fieldValid.pattern().format())
                        .matcher(filterFields.get(getFieldName(fieldValid.fieldName(), 0))).matches())
                .map(fieldValid -> (StringUtils.isEmpty(fieldValid.message()))
                        ? getFieldName(fieldValid.fieldName(), 1) + " (이)가 유효하지 않습니다."
                        : fieldValid.message())
                .toList();
        List<String> invalidList = Stream.concat(notPresentList.stream(), inValidFieldList.stream()).toList();
        if (invalidList.size() > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(CommonErrorCode.REQUIRED_PARAMETER.message())
                    .addPropertyNode(String.join(", ", invalidList))
                    .addConstraintViolation();
        }
        return invalidList.size() == 0;
    }

    /**
     * ':' 로 구분된 fieldName 에서 원하는 부분을 추출, example) userName:사용자명
     *
     * @param fieldName fieldName
     * @param index     추출 하고자 하는 fieldName index, 0:eng, 1:kor
     * @author GEONLEE
     * @since 2024-07-10
     */
    private String getFieldName(String fieldName, int index) {
        return (fieldName.contains(":")) ? fieldName.split(":")[index].trim() : fieldName.trim();
    }
}
