package com.aljjabaegi.api.common.util;

/**
 * @author GEONLEE
 * @since 2024-07-10
 */
public enum RegularExpression {

    ALL_PASS(".*"),
    ONLY_NUMBER("^[0-9]+$"),
    ONLY_ALPHABET("^[a-zA-Z]+$"),
    ONLY_KOREAN("^[가-힣]+$"),

    ONLY_UPPER("^[A-Z]+$"),
    ONLY_LOWER("^[a-z]+$"),

    ONLY_CAR_NUMBER("^[가-힣0-9]*[가-힣]+[0-9]{4}$"),

    DATE("^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$"),
    DATE_FORMAT("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"),
    DATE_RANGE("^\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01]),\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$"),
    DATETIME("^\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$"),
    DATETIME_FORMAT("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"),
    DATETIME_RANGE("^\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])," +
            "\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$");

    private final String format;

    RegularExpression(String format) {
        this.format = format;
    }

    public String format() {
        return this.format;
    }

}
