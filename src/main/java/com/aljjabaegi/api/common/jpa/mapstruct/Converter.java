package com.aljjabaegi.api.common.jpa.mapstruct;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * mapstruct 용 static converter
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
public class Converter {
    /**
     * String Y, N 을 받아 true, false 로 리턴
     *
     * @param yn Y, N String
     * @return true or false
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    public static Boolean stringToBoolean(String yn) {
        return StringUtils.equals(yn, "Y");
    }

    /**
     * true, false 를 받아 Y, N 으로 리턴
     *
     * @param is true or false
     * @return Y or N
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    public static String booleanToString(Boolean is) {
        return (is) ? "Y" : "N";
    }

    /**
     * 패스워드 인코딩
     *
     * @param password 인코딩 전 패스워드
     * @return 인코딩된 패스워드
     * @author GEONLEE
     * @since 2024-04-03
     */
    public static String encodePassword(String password) {
        PasswordEncoder passwordEncoder = ApplicationContextHolder.getContext().getBean(PasswordEncoder.class);
        return passwordEncoder.encode(password);
    }
}
