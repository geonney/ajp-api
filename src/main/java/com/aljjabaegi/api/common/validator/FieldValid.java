package com.aljjabaegi.api.common.validator;

import com.aljjabaegi.api.common.util.RegularExpression;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GEONLEE
 * @since 2024-07-10
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValid {
    String fieldName();
    RegularExpression pattern();
}
