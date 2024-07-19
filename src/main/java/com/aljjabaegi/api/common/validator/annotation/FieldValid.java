package com.aljjabaegi.api.common.validator.annotation;

import com.aljjabaegi.api.common.validator.enumeration.RegularExpression;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 동적 Field 유효성 체크 annotation<br />
 * \@DynamicValid(essentialFields = {"memberName:사용자명 "}, fieldValidations = {
 * \@FieldValid(fieldName = "memberName:사용자명", pattern = RegularExpression.ONLY_KOREAN),
 * \@FieldValid(fieldName = "age:나이", pattern = RegularExpression.ONLY_NUMBER, message = "나이는 숫자여야 합니다.")
 * }) DynamicRequest dynamicRequest
 *
 * @author GEONLEE
 * @since 2024-07-10
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValid {
    String fieldName();

    RegularExpression pattern() default RegularExpression.ALL_PASS;

    int length() default Integer.MAX_VALUE;

    String message() default "";
}
