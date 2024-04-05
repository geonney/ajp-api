package com.aljjabaegi.api.common.jpa.mapstruct;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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

    public static LocalDateTime dateTimeStringToLocalDateTime(String dateString) throws DateTimeParseException, IllegalArgumentException {
        if (dateString != null && !dateString.equals("")) {
            String replacedDateString = getStringToNumbers(dateString);
            DateTimeFormatter toTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);
            return LocalDateTime.parse(replacedDateString, toTimeFormatter);
        } else {
            return null;
        }
    }

    public static LocalDate dateStringToLocalDate(String dateString) throws DateTimeParseException, IllegalArgumentException {
        if (dateString != null && !dateString.equals("")) {
            String replacedDateString = getStringToNumbers(dateString);
            return LocalDate.parse(replacedDateString, DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA));
        } else {
            return null;
        }
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) throws DateTimeParseException {
        if (localDateTime != null) {
            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
        } else {
            return null;
        }
    }

    public static String localDateToString(LocalDateTime localDate) throws DateTimeParseException {
        if (localDate != null) {
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA));
        } else {
            return null;
        }
    }

    public static LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    public static String getStringToNumbers(String str) {
        return str.replaceAll("[^0-9]+", "");
    }

}
