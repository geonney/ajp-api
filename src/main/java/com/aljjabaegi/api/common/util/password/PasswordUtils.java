package com.aljjabaegi.api.common.util;

import com.aljjabaegi.api.common.util.password.enumeration.PasswordLevel;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 패스워드 유효성 검증
 *
 * @author GEONLEE
 * @since 2024-04-17
 */
public class PasswordUtils {

    // 8자리 이상
    private static final String DIGITS_REGEXP = ".${8,}";
    // 하나 이상의 대문자
    private static final String UPPERCASE_REGEXP = ".*[A-Z].*";
    // 하나 이상의 숫자
    private final String NUMBER_REGEXP = ".*[0-9].*";
    // 하나 이상의 특수문자
    private final String SPECIAL_CHARACTER_REGEXP = ".*[^a-zA-Z0-9가-힣].*";

    /**
     * 비밀번호 유효성 검증 (Use PasswordLevel)
     *
     * @author GEONLEE
     * @since 2024-04-17
     */
    public static boolean validPassword(PasswordLevel passwordLevel, String password) {
        Pattern passwordPattern = Pattern.compile(passwordLevel.getRegexp());
        return passwordPattern.matcher(password).matches();
    }

    /**
     * 비밀번호 유효성 검증
     *
     * @author GEONLEE
     * @since 2024-04-17
     */
    public static boolean validPassword(String password) {
        List<String> validProcess = List.of(DIGITS_REGEXP, UPPERCASE_REGEXP);
        Pattern passwordPattern = Pattern.compile();
        return passwordPattern.matcher(password).matches();
    }
}
