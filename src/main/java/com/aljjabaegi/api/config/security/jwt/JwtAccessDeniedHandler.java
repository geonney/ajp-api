package com.aljjabaegi.api.config.security.jwt;


import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.code.ErrorCode;
import com.aljjabaegi.api.common.response.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 403 FORBIDDEN Exception 처리 응답 클래스 - 클라이언트 접근 거부<br />
 * HTTP Status 는 200으로, 응답 코드는 FORBIDDEN 으로 전송<br />
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        PrintWriter writer = response.getWriter();
        ErrorCode errorCode = CommonErrorCode.FORBIDDEN;
        LOGGER.info("{}, URI: {}", errorCode, request.getRequestURI());

        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(new Gson().toJson(ErrorResponse.builder()
                    .status(errorCode.status())
                    .message(errorCode.message()).build()));
        } catch (NullPointerException e) {
            LOGGER.error("Create fail to message", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}