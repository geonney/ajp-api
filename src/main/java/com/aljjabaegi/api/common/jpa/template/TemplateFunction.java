package com.aljjabaegi.api.common.jpa.template;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;

/**
 * Database method class<br />
 * Querydsl 사용 시 DB 내장 메서드를 사용할 때 추가하여 활용한다.<br />
 * Template 종류<br />
 * - StringTemplate<br />
 * - NumberTemplate<br />
 * - BooleanTemplate<br />
 * - DateTemplate<br />
 * - EnumTemplate<br />
 * - ComparableTemplate<br />
 *
 * @author GEONLEE
 * @since 2024-05-07
 */
public class TemplateFunction {

    /**
     * TO_CHAR 메서드 호출 메서드<br />
     * TemplateFunction.TO_CHAR(m_op_upd_ver.applyDate, "YYYY-MM-DD HH24:MI:SS").as("applyDate")
     *
     * @param column QEntity column / ex) m_op_upd_ver.applyDate
     * @param format 변환 format / ex) "YYYY-MM-DD HH24:MI:SS"
     * @return StringTemplate
     * @author GEONLEE
     * @since 2024-05-07
     */
    public static StringTemplate TO_CHAR(Object column, String format) {
        return Expressions.stringTemplate("TO_CHAR({0}, '{1s}')", column, format);
    }

    /**
     * Integer type 변환 메서드
     *
     * @param stringPath path
     * @return NumberTemplate
     * @author GEONLEE
     * @since 2024-07-10
     */
    public static NumberTemplate<Integer> TO_NUMBER(StringPath stringPath) {
        return Expressions.numberTemplate(Integer.class, "CAST({0} AS INTEGER)", stringPath);
    }
}
