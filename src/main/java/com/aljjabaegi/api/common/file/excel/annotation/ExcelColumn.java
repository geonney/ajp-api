package kr.co.neighbor21.neighborApi.common.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @description 커스텀 어노테이션으로 적용
 * @autor yh.kim
 * @see java.lang.annotation.Annotation
 * 2024-07-08 GEONLEE - cellWidth 추가<br /
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NsColumn {

    int index();

    // 엑셀에 표출된 이름
    String name();

    int cellWidth() default 200;

    @Deprecated
    String example() default "";

    String description() default "";

    String defaultValue() default "";

    //    String displayName() default "";
    boolean trim() default false;

    boolean emptyValueIsNull() default false;

    @Deprecated
    boolean primary() default false;

    boolean hide() default false;
}
