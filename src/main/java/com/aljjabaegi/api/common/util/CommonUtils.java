package com.aljjabaegi.api.common.util;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import com.aljjabaegi.api.common.response.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.ServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 공통 Utils
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
public class CommonUtils {

    /**
     * Property 값에 접근하는 @Value 는 spring bean 에서 사용하는 용도이므로, static class 나 다른 곳에서 property 값이 필요할 때 활용한다.
     *
     * @param key property key
     * @return string value
     * @author GEONLEE
     * @since 2023-08-08<br />
     */
    public static String getPropertyValue(String key) {
        return ApplicationContextHolder.getContext().getEnvironment().getProperty(key);
    }

    /**
     * Response writer 메서드
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    public static void setResponseWriter(
            ServletResponse response, String resultCode, String resultMsg) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(new Gson().toJson(ErrorResponse.builder()
                    .status(resultCode)
                    .message(resultMsg)
                    .build()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
