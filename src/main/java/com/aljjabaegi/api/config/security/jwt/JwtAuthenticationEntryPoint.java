package com.aljjabaegi.api.config.security.jwt;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import static com.aljjabaegi.api.config.security.jwt.TokenProvider.AUTHORIZATION_FAIL_TYPE;


/**
 * 401 UNAUTHORIZED (인증 실패) 처리용 응답 클래스
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        CommonErrorCode errorCode = (CommonErrorCode) request.getSession().getAttribute(AUTHORIZATION_FAIL_TYPE);
        if (errorCode != CommonErrorCode.DUPLICATION_LOGIN) {
            errorCode = CommonErrorCode.UNAUTHORIZED;
        }
        CommonUtils.setResponseWriter(response, errorCode.status(), errorCode.message());
    }
}