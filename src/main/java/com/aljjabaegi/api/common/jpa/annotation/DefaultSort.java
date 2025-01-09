package com.aljjabaegi.api.common.jpa.annotation;

import com.aljjabaegi.api.common.request.enumeration.SortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Entity 기본 정렬 컬럼 설정용 annotation<br />
 * Entity class 레벨에 추가하여 사용
 *
 * @author GEONLEE
 * @since 2024-04-24<br />
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultSort {
    String[] columnName();

    SortDirection[] direction();

    String[] alias() default "";
}
