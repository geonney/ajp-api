package com.aljjabaegi.api.common.file.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Excel download 시 record 속성 위에 추가되는 컬럼 설정 어노테이션
 *
 * @author GEONLEE
 * @since 2024-07-09
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    int index(); // 열 인덱스

    String name(); // 열 헤더 명

    int cellWidth() default 200; // 열 넓이
}
