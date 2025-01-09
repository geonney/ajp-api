package com.aljjabaegi.api.config.security.jwt;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.util.CommonUtils;
import com.aljjabaegi.api.config.security.jwt.exception.DuplicateLoginException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


/**
 * 401 UNAUTHORIZED (인증 실패) 처리용 응답 클래스
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        CommonErrorCode errorCode = CommonErrorCode.UNAUTHORIZED;
        if (authException instanceof DuplicateLoginException) {
            // 중복 로그인 예외 처리
            errorCode = CommonErrorCode.DUPLICATION_LOGIN;
        } else if (authException instanceof AccountExpiredException) {
            // Access, Refresh token 모두 만료
            errorCode = CommonErrorCode.EXPIRED_TOKEN;
        }
        log.error(authException.getMessage(), authException);
        CommonUtils.setResponseWriter(response, errorCode.status(), errorCode.message());
    }
}