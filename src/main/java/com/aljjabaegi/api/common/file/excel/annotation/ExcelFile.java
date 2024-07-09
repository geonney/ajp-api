package com.aljjabaegi.api.common.file.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Excel download record 에 추가하는 어노테이션
 */
@Documented
@Target({TYPE, CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelFile {

    String sheetName() default "";

}
