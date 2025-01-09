package com.aljjabaegi.api.common.jpa.dynamicSearch.converter.enumeration;

/**
 * @author GEONLEE
 * @since 2025-01-09
 */
public enum DateType {
    Y("yyyy"),
    YM("yyyyMM"),
    YMD("yyyyMMdd"),
    YMDH("yyyyMMddHH"),
    YMDHM("yyyyMMddHHmm"),
    YMDHMS("yyyyMMddHHmmss"),

    //formatted
    YM_F("yyyy-MM"),
    YMD_F("yyyy-MM-dd"),
    YMDH_F("yyyy-MM-dd HH"),
    YMDHM_F("yyyy-MM-dd HH:mm"),
    YMDHMS_F("yyyy-MM-dd HH:mm:ss");

    private final String pattern;

    DateType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }
}
