package com.aljjabaegi.api.config.security.jwt;


import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 403 FORBIDDEN Exception 처리 응답 클래스 - 클라이언트 접근 거부<br />
 * HTTP Status 는 200으로, 응답 코드는 FORBIDDEN 으로 전송<br />
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 * 2024-04-24 GEONLEE - 응답 및 로깅 처리 추가<br />
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String memberId = userDetails.getUsername();
        LOGGER.error("'{}' member access denied at {}", memberId, request.getRequestURI());
        CommonErrorCode errorCode = CommonErrorCode.FORBIDDEN;
        CommonUtils.setResponseWriter(response, errorCode.status(), errorCode.message());
    }
}