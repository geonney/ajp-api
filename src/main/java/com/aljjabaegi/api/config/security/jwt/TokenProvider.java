package com.aljjabaegi.api.config.security.jwt;

import com.aljjabaegi.api.config.security.jwt.record.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Token 의 생성, 인증정보 조회, 유효성 검증 등의 역할을 하는 클래스<br />
 * jwt-key 를 환경변수에 등록한다.
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
@Component
public class TokenProvider {
    public static final String AUTHORIZATION_FAIL_TYPE = "AJP_AUT_FT";
    protected static final long ACCESS_EXPIRATION_MILLISECONDS = 24 * (1000 * 3600); //24시간
    protected static final long REFRESH_EXPIRATION_MILLISECONDS = (24 * 7) * (1000 * 3600); //일주일
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    private final String jwtCookieName = "AJP_AUT";
    private final String authorityClaimName = "AJP_ATC";
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 설정파일의 jwt.secret-key 를 사용하여 secretKey 생성<br />
     * jwt key ->  base64 encoding
     *
     * @param secretKey base64 encoded key
     * @author GEONLEE
     * @since 2024-04-02
     */
    public TokenProvider(@Value("${security.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
    }

    /**
     * Generate token
     *
     * @param authentication security Authentication
     * @param milliseconds   만료 초
     * @return 생성된 token
     * @author GEONLEE
     * @since 2024-04-02<br />
     */
    public String generateToken(Authentication authentication, long milliseconds) {
        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + milliseconds);
        return Jwts.builder()
                .subject(authentication.getName())
                .issuer(this.issuer)
                .signWith(this.secretKey)
                .claim(authorityClaimName, authority)
                .expiration(validity)
                .compact();
    }

    /**
     * Generate Access and refresh token
     *
     * @param authentication Security 인증정보
     * @return TokenResponse 토큰 응답 record
     * @author GEONLEE
     * @since 2022-11-11<br />
     */
    public TokenResponse generateTokenResponse(Authentication authentication) {
        return TokenResponse.builder()
                .token(generateToken(authentication, ACCESS_EXPIRATION_MILLISECONDS))
                .refreshToken(generateToken(authentication, REFRESH_EXPIRATION_MILLISECONDS))
                .tokenType(TOKEN_TYPE)
                .expirationSeconds(ACCESS_EXPIRATION_MILLISECONDS)
                .build();
    }

    /**
     * token 에서 claims 추출
     *
     * @param token token 값
     * @return token claims
     * @author GEONLEE
     * @since 2024-04-02
     */
    private Claims getClaimsFromToken(String token) {
        return (Claims) this.jwtParser.parse(token).getPayload();
    }

    /**
     * token 에서 Id 추출
     *
     * @param token token 값
     * @return user id
     * @author GEONLEE
     * @since 2024-04-02
     */
    public String getIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * token 에서 권한 추출 하여 security Authentication 생성
     *
     * @param token token 값
     * @return Authentication security authentication
     * @author GEONLEE
     * @since 2024-04-02
     */
    public Authentication generateAuthorityFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority((String) claims.get(authorityClaimName)));
        User principal = new User(claims.getSubject(), "", grantedAuthorities);
        return new UsernamePasswordAuthenticationToken(principal, token, grantedAuthorities);
    }

    /**
     * 쿠키에 Access Token 을 생성한 token 으로 갱신한다.
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    public void renewalAccessTokenInCookie(HttpServletResponse httpServletResponse, String newAccessToken) {
        Cookie cookie = new Cookie(jwtCookieName, newAccessToken);
        cookie.setHttpOnly(true);
        cookie.setPath(this.contextPath);
        httpServletResponse.addCookie(cookie);
    }

    /**
     * Cookie 의 토큰을 만료시킨다.
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    public void expirationToken(HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(this.contextPath);
        httpServletResponse.addCookie(cookie);
    }

    /**
     * Cookie 에서 AccessToken 추출
     *
     * @param httpServletRequest request
     * @author GEONLEE
     * @since 2024-04-02
     */
    public String getTokenFromCookie(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String requestURI = httpServletRequest.getRequestURI().replace(this.contextPath, "");
        if (cookies != null) {
            Optional<String> optionalAccessToken = Arrays.stream(cookies)
                    .filter(cookie -> jwtCookieName.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
            if (optionalAccessToken.isPresent()) {
                return doXssFilter(optionalAccessToken.get());
            }
        }
        LOGGER.error("Access token in cookie does not exist. request URI: {}", requestURI);
        return null;
    }

    /**
     * HttpServletRequest 에서 refresh token 추출
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     */
    public String getRefreshTokenFromRequest(HttpServletRequest httpServletRequest) throws NullPointerException {
        String requestURI = httpServletRequest.getRequestURI().replace(this.contextPath, "");
        String bearerToken = httpServletRequest.getHeader("Authorization");
        String token = bearerToken.substring(7);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") && !"null".equals(token)) {
            return bearerToken.substring(7);
        }
        LOGGER.error("Refresh token in header does not exist. request URI: {}", requestURI);
        return null;
    }

    /**
     * token 유효성 검증
     *
     * @param token token 값
     * @author GEONLEE
     * @since 2024-04-02
     */
    public boolean validateToken(String token) {
        try {
            this.jwtParser.parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            LOGGER.info("Invalid jwt signature.");
        } catch (ExpiredJwtException e) {
            LOGGER.error("Access token is expired.");
        } catch (UnsupportedJwtException e) {
            LOGGER.info("This jwt token is not supported.");
        } catch (IllegalArgumentException e) {
            LOGGER.info("Invalid jwt token.");
        }
        return false;
    }

    /**
     * 크로스 스크립팅 방지
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     */
    private String doXssFilter(String origin) {
        return origin.replaceAll("'", "&#x27;").replaceAll("\"", "&quot;").replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;").replaceAll("/", "&#x2F;").replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;").replaceAll("&", "&amp;");
    }
}
