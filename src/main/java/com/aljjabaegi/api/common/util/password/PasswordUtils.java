package com.aljjabaegi.api.common.util.password;

import com.aljjabaegi.api.common.enumeration.RegExp;
import com.aljjabaegi.api.common.util.password.enumeration.PasswordLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 패스워드 유효성 검증
 *
 * @author GEONLEE
 * @since 2024-04-17
 */
public class PasswordUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtils.class);

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
     * 비밀번호 유효성 검증<br />
     * 검정하고자하는 정규식을 리스트에 추가해서 사용
     *
     * @param password plain text
     * @return 유효 여부
     * @author GEONLEE
     * @since 2024-04-17
     */
    public static boolean validPassword(String password) {
        boolean result = true;
        List<RegExp> validProcess = List.of(RegExp.DIGITS, RegExp.UPPERCASE, RegExp.NUMBER);
        for (RegExp regExp : validProcess) {
            if (!regExp.getPatten().matcher(password).matches()) {
                LOGGER.error(regExp.getMessage());
                result = false;
                break;
            }
        }
        return result;
    }
}
