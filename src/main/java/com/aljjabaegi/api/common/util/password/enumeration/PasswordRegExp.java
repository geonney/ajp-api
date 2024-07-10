package com.aljjabaegi.api.common.util.password.enumeration;

import java.util.regex.Pattern;

/**
 * @author GEONLEE
 * @since 2024-04-17
 */
public enum PasswordRegExp {
    // 8자리 이상
    DIGITS(".{8,}$", "Not more than 8 digits"),
    // 하나 이상의 대문자
    UPPERCASE(".*[A-Z].*", "Does not contain capital letters"),
    // 하나 이상의 숫자
    NUMBER(".*[0-9].*", "Does not contain numbers"),
    // 하나 이상의 특수문자
    SPECIAL_CHARACTER(".*[^a-zA-Z0-9가-힣].*", "Does not contain special characters");

    private final String value;
    private final String message;

    PasswordRegExp(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public Pattern getPatten() {
        return Pattern.compile(this.value);
    }

    public String getMessage() {
        return this.message;
    }
}
