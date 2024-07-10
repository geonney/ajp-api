package com.aljjabaegi.api.common.util;

/**
 * @author GEONLEE
 * @since 2024-07-10
 */
public enum RegularExpression {
    ONLY_NUMBER("^[0-9]+$"),
    ONLY_ALPHABET("^[a-zA-Z]+$"),
    ONLY_KOREAN("^[가-힣]+$"),

    ONLY_UPPER("^[A-Z]+$"),
    ONLY_LOWER("^[a-z]+$"),

    ONLY_CAR_NUMBER("^[가-힣0-9]*[가-힣]+[0-9]{4}$");

    private final String format;

    RegularExpression(String format) {
        this.format = format;
    }

    public String format() {
        return this.format;
    }

}
