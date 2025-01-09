package com.aljjabaegi.api.common.converter;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import com.aljjabaegi.api.entity.enumerated.UseYn;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * mapstruct 용 static converter
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-17 GEONLEE - Add getToday method<br />
 * 2024-04-29 GEONLEE - Add stringToEnum<br />
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
     * String UseYn 을 받아 true, false 로 리턴
     *
     * @param useYn UseYn.Y, UseYn.N
     * @return true or false
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    public static Boolean useYnToBoolean(UseYn useYn) {
        return useYn == UseYn.Y;
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
     * true, false 를 받아 UseYn 으로 리턴
     *
     * @param is true or false
     * @return UseYn.Y, UseYn.N
     * @author GEONLEE
     * @since 2024-04-07<br />
     */
    public static UseYn booleanToUseYn(Boolean is) {
        return (is) ? UseYn.Y : UseYn.N;
    }

    /**
     * Spring security 에 적용된 Password Encoder 로 패스워드 인코딩<br />
     *
     * @param password 인코딩 전 패스워드
     * @return 인코딩된 패스워드
     * @author GEONLEE
     * @since 2024-04-03<br />
     * RSA 로 인코딩되어 전달된 Password 를 복호화하고, 다시 인코딩해서 전달하게 수정.
     */
    public static String encodePassword(String password) {
        RsaProvider rsaProvider = ApplicationContextHolder.getContext().getBean(RsaProvider.class);
        PasswordEncoder passwordEncoder = ApplicationContextHolder.getContext().getBean(PasswordEncoder.class);
        return passwordEncoder.encode(rsaProvider.decrypt(password));
    }

    /**
     * String value to enum value
     *
     * @param fieldType enum class
     * @param value     enum String value
     * @return Enum
     * @author GEONLEE
     * @since 2024-04-29
     */
    public static Enum<?> stringToEnum(Class<?> fieldType, String value) {
        Method method;
        try {
            method = fieldType.getDeclaredMethod("valueOf", String.class);
            return (Enum<?>) method.invoke(null, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }
}
