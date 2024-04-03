package com.aljjabaegi.api.config.security.jwt;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.util.CommonUtils;
import com.aljjabaegi.api.config.security.jwt.record.JwtValidDto;
import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.aljjabaegi.api.config.security.jwt.TokenProvider.ACCESS_EXPIRATION_MILLISECONDS;
import static com.aljjabaegi.api.config.security.jwt.TokenProvider.AUTHORIZATION_FAIL_TYPE;
import static com.aljjabaegi.api.config.security.spring.SecurityConfig.IGNORE_URIS;

/**
 * JWT Filter<br />
 * Cookie 에서 Access Token 을 추출해 유효성 검증 및 중복 로그인 여부를 체크<br />
 * Access Token 이 만료된 경우 만료 코드를 프론트로 전달. 프론트는 Access Token 만료 시<br />
 * 로그인 시 받은 Refresh Token 을 Header Bearer 에 담에 전송.<br />
 * Cookie Access Token 만료 -> Header Bearer Refresh Token 유효 -> Access Token 갱신, 만료 시 로그인 페이지로 이동<br />
 * Access Token 이 DB 의 Access Token 과 다를 경우 중복 로그인으로 판단. Refresh Token 도 동일<br />
 * 최종 로그인 한 token 이 유효<br />
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    private final List<String> ignoreUris = List.of(IGNORE_URIS);
    private final TokenProvider tokenProvider;
    private final String contextPath = CommonUtils.getPropertyValue("server.servlet.context-path");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestURI = httpServletRequest.getRequestURI().replace(contextPath, "");
        if (!ignoreUris.contains(requestURI) && !requestURI.startsWith("/swagger-") && !requestURI.startsWith("/api-docs")) {
            LOGGER.info("Request URI : '{}', Start to check access token. ▼", requestURI);
            String accessToken = null;
            /*1. Cookie 에서 Access Token 추출 (NS_AUT)*/
            accessToken = tokenProvider.getTokenFromCookie(httpServletRequest);
            JwtValidDto valid = new JwtValidDto(false, null, accessToken);
            /*2. Access Token 유효성 체크*/
            if (StringUtils.hasText(accessToken)) {
                LOGGER.info("Check Access Token validation");
                checkTokenValidity(valid, httpServletRequest, httpServletResponse);
            }
            /*3. 중복 로그인인지 체크*/
            if (valid.isValid()) checkDuplicationLogin(valid, httpServletRequest);
            /*4. Spring security 에 권한 정보 저장
             * 권한 정보가 없을 경우 JwtAuthenticationEntryPoint 로 전달.(security config 에 설정)*/
            if (valid.isValid()) {
                LOGGER.info("User's token validation success: '{}'", valid.getUserId());
                Authentication authentication = tokenProvider.generateAuthorityFromToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * AccessToken 유효성 체크<br />
     * Cookie 내 Access Token 의 유효성을 검사하고 header 에 refresh Token 이 유효할 경우<br />
     * 새로운 토큰을 생성하여 Cookie 의 Access token 을 갱신<br />
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    private void checkTokenValidity(
            JwtValidDto valid, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (!StringUtils.hasText(valid.getAccessToken()) || !tokenProvider.validateToken(valid.getAccessToken())) {
            valid.setValid(false);
            String refreshToken = null;
            /*Access Token 이 만료 되었을 경우 RefreshToken 을 확인*/
            try {
                refreshToken = tokenProvider.getRefreshTokenFromRequest(httpServletRequest);
                if (StringUtils.hasText(refreshToken) || tokenProvider.validateToken(refreshToken)) {
                    /*refreshToken 에서 권힌 정보를 추출해 새로운 Access Token 생성*/
                    Authentication authentication = tokenProvider.generateAuthorityFromToken(refreshToken);
                    String userId = tokenProvider.getIdFromToken(refreshToken);
                    valid.setUserId(userId);
                    String newAccessToken = tokenProvider.generateToken(authentication, ACCESS_EXPIRATION_MILLISECONDS);
                    /*쿠키 Access Token 정보 갱신*/
                    tokenProvider.renewalAccessTokenInCookie(httpServletResponse, newAccessToken);
                    /*Refresh Token 으로 User 를 조회 해  Access Token 갱신*/
                    MemberRepository memberRepository = ApplicationContextHolder.getContext().getBean(MemberRepository.class);
                    memberRepository.findOneByMemberIdAndRefreshToken(valid.getUserId(), refreshToken)
                            .ifPresentOrElse(member -> {
                                member.setAccessToken(newAccessToken);
                                memberRepository.save(member);
                                valid.setAccessToken(newAccessToken);
                                valid.setValid(true);
                                LOGGER.info("Renew user's access token with refresh token: '{}'", valid.getUserId());
                            }, () -> LOGGER.error("User's refresh token is different. (Duplicated login): '{}'", valid.getUserId()));
                }
            } catch (NullPointerException e) {
                LOGGER.error("Refresh token extraction failed.");
            }
        } else {
            String userId = tokenProvider.getIdFromToken(valid.getAccessToken());
            valid.setValid(true);
            valid.setUserId(userId);
        }
    }

    /**
     * 중복 로그인 여부 체크<br />
     * DB의 Access Token 과 비교<br />
     *
     * @author GEONLEE
     * @since 2024-03-28
     */
    private void checkDuplicationLogin(JwtValidDto valid, HttpServletRequest httpServletRequest) {
        MemberRepository userRepository = ApplicationContextHolder.getContext().getBean(MemberRepository.class);
        Optional<Member> optionalMember = userRepository.findOneByMemberIdAndAccessToken(valid.getUserId(), valid.getAccessToken());
        if (optionalMember.isEmpty()) {
            LOGGER.error("User's access token is different. (Duplicated login): '{}'", valid.getUserId());
            valid.setValid(false);
            httpServletRequest.getSession().setAttribute(AUTHORIZATION_FAIL_TYPE, CommonErrorCode.DUPLICATION_LOGIN);
        }
    }
}