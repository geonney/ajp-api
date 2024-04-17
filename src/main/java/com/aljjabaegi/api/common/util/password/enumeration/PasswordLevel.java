package com.aljjabaegi.api.common.util.password.enumeration;

/**
 * password level
 *
 * @author GEONLEE
 * @since 2024-04-17
 */
public enum PasswordLevel {
    // 8자리 이상, 하나 이상의 문자, 숫자
    LEVEL1("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"),
    // 8자리 이상, 하나 이상의 문자, 숫자, 특수문자
    LEVEL2("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"),
    // 8자리 이상, 하나 이상의 대문자, 소문자, 숫자, 특수문자
    LEVEL3("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    private final String regexp;

    PasswordLevel(String regexp) {
        this.regexp = regexp;
    }

    /**
     * @return 정규식
     */
    public String getRegexp() {
        return this.regexp;
    }
}