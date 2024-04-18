package com.aljjabaegi.api.common.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Setting up searchable field
 *
 * @author GEONLEE
 * @since 2024-04-11<br />
 * 2024-04-18 GEONLEE - Add columnPath, DynamicFilter 를 사용할 경우 연관관계에 있는 Entity 정보 조회 관련 이슈 #3 보완<br />
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchableField {
    String[] columnPath() default "";
}
