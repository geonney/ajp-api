package com.aljjabaegi.api.common.jpa.dynamicSearch.converter;

import com.aljjabaegi.api.common.jpa.dynamicSearch.converter.enumeration.DateType;
import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author GEONLEE
 * @since 2025-01-09
 */
public class DateConverter {

    public static DateTimeFormatter getFormatter(DateType dateType) {
        return DateTimeFormatter.ofPattern(dateType.getPattern(), Locale.KOREA);
    }

    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        if (StringUtils.isEmpty(dateTimeString)) {
            return null;
        }
        String onlyNumber = toNumber(dateTimeString);
        if (onlyNumber.length() != 14) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, dateTimeString + " must be 14 digits long.");
        }
        DateTimeFormatter formatter = getFormatter(DateType.YMDHMS);
        return LocalDateTime.parse(onlyNumber, formatter);
    }

    public static LocalDate toLocalDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        String onlyNumber = toNumber(dateString);
        if (onlyNumber.length() != 8) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, dateString + " must be 8 digits long.");
        }
        DateTimeFormatter formatter = getFormatter(DateType.YMD);
        return LocalDate.parse(onlyNumber, formatter);
    }

    public static String toString(LocalDateTime localDateTime, DateType dateType) {
        if (ObjectUtils.isEmpty(localDateTime)) {
            return null;
        }
        return localDateTime.format(getFormatter(dateType));
    }

    public static String toString(LocalDate localDate, DateType dateType) {
        if (ObjectUtils.isEmpty(localDate)) {
            return null;
        }
        return localDate.format(getFormatter(dateType));
    }

    private static String toNumber(String str) {
        return str.replaceAll("[^0-9]+", "");
    }
}
