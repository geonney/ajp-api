package com.aljjabaegi.api.common.jpa.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DB column type 이 문자형인데, 데이터가 숫자만 있는 경우 정렬 문제 개선용 어노테이션<br />
 * 1, 11, 12, 13, 2, 3 -> 1, ,2, 3, 11, 12, 13 정렬<br />
 * 추후 number type 에 따른 cast 추가 예정, default INTEGER<br />
 * <p>
 * 데이터를 cast 하는 방식이기 때문에 data 에 숫자를 제외한 값이 있을 경우 exception 이 발생하니 주의!<br />
 * java.sql.SQLException: JDBC-5074:Given string does not represent a number in proper format.
 *
 * @author GEONLEE
 * @since 2024-07-10<br />
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumericOrder {
    String cast() default "INTEGER";
}
