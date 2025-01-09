package com.aljjabaegi.api.config.security.jwt;

import com.aljjabaegi.api.common.util.CommonUtils;
import com.aljjabaegi.api.config.security.jwt.enumeration.TokenType;
import com.aljjabaegi.api.config.security.jwt.exception.NoTokenException;
import com.aljjabaegi.api.config.security.jwt.record.JwtValidDto;
import com.aljjabaegi.api.domain.member.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

import static com.aljjabaegi.api.config.security.jwt.TokenProvider.ACCESS_EXPIRATION_MILLISECONDS;
import static com.aljjabaegi.api.config.security.springSecurity.SpringSecurityConfig.IGNORE_URIS;

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
 * 2024-04-04 GEONLEE - Access Token 에서 권한 추출 부분, Refresh Token 검증 부분 버그 수정<br />
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    private final List<String> ignoreUris = List.of(IGNORE_URIS);
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final MemberRepository memberRepository;
    private final String contextPath = CommonUtils.getPropertyValue("server.servlet.context-path");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestURI = httpServletRequest.getRequestURI().replace(contextPath, "");

        if (!ignoreUris.contains(requestURI) && !requestURI.startsWith("/swagger-") && !requestURI.startsWith("/api-docs")) {
            LOGGER.info("Request URI : '{}', Start to check access token. ▼", requestURI);
            /*1. Session Cookie 에서 Access Token 추출 */
            String accessToken = tokenProvider.getTokenFromCookie(httpServletRequest);

            /*2. Access Token 유무 확인 */
            if (!StringUtils.hasText(accessToken)) {
                jwtAuthenticationEntryPoint.commence(httpServletRequest,
                        httpServletResponse, new NoTokenException("No Access token in session cookie."));
                return;
            }
            JwtValidDto valid = new JwtValidDto(false, null, accessToken);

            /*3. Access token 유효성 체크 로직
             * 3-1. Access Token 유효성 체크
             * 3-2. 만료일 경우에만 Access Token 으로 DB 의 Refresh Token 확인
             * 3-3. 조회된 Refresh Token 이 없다면 '중복 로그인' 으로 판단. 코드 전송 (ERR_AT_04)
             * 3-3. Refresh Token 유효성 체크
             * 3-4. 만료일 경우 만료 코드 전송 (ERR_AT_03)
             * 3-5. 만료되지 않았을 경우 Access, Refresh Token 재발행 (DB 정보 update) 및 기존 로직 수행
             * */
            if (!checkTokenValidity(valid, httpServletRequest, httpServletResponse)) {
                return;
            }

            /*4. Spring security 에 권한 정보 저장
             * 권한 정보가 없을 경우 JwtAuthenticationEntryPoint 로 전달.(security config 에 설정)*/
            log.info("User's token validation success: '{}'", valid.getMemberId());
            Authentication authentication = tokenProvider.generateAuthorityFromToken(valid.getAccessToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * AccessToken 유효성 체크<br />
     * Cookie 내 Access Token 의 유효성을 검사하고 header 에 refresh Token 이 유효할 경우<br />
     * 새로운 토큰을 생성하여 Cookie 의 Access token 을 갱신<br />
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     * 2024-04-15 GEONLEE - token 갱신 시 member id 와 refresh token 체크를 먼저 하도록 변경
     */
    private boolean checkTokenValidity(
            JwtValidDto valid, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            if (tokenProvider.validateToken(TokenType.ACCESS_TOKEN, valid.getAccessToken())) {
                String memberId = tokenProvider.getIdFromToken(valid.getAccessToken());
                valid.setValid(true);
                valid.setMemberId(memberId);
                return true;
            } else {
                valid.setValid(false);
            }
        } catch (ExpiredJwtException e) {
            // Access Token 이 만료되었을 경우 Access Token 을 갖고 있는 Refresh Token 을 DB 에서 조회
            String refreshToken = tokenProvider.getRefreshTokenFromDatabase(httpServletRequest, httpServletResponse);
            try {
                if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(TokenType.REFRESH_TOKEN, refreshToken)) {
                    //Refresh Token 이 유효할 경우 Token 갱신
                    String userId = tokenProvider.getIdFromToken(refreshToken);
                    memberRepository.findById(userId).ifPresentOrElse(user -> {
                        valid.setMemberId(userId);
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                        String newAccessToken = tokenProvider.generateToken(authentication, ACCESS_EXPIRATION_MILLISECONDS, user);
                        valid.setAccessToken(newAccessToken);
                        tokenProvider.renewalAccessTokenInCookie(httpServletResponse, newAccessToken);
                        user.setAccessToken(newAccessToken);
                        memberRepository.save(user);
                        log.info("Token reissue. -> {}", user.getMemberId());
                    }, () -> valid.setValid(false));
                    valid.setValid(true);
                    return true;
                } else {
                    // DB에 Refresh Token 이 없거나 유효성 체크에 실패한 경우
                    jwtAuthenticationEntryPoint.commence(httpServletRequest,
                            httpServletResponse, new NoTokenException("No Refresh token in database."));
                }
            } catch (ExpiredJwtException eje) {
                // Access Token 만료 && Refresh Token 만료 일 경우 실제 만료 처리
                jwtAuthenticationEntryPoint.commence(httpServletRequest,
                        httpServletResponse, new AccountExpiredException("Account Expired."));
            }
        }
        return false;
    }
}