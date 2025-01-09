package com.aljjabaegi.api.config.security.jwt;

import com.aljjabaegi.api.config.security.jwt.enumeration.TokenType;
import com.aljjabaegi.api.config.security.jwt.exception.DuplicateLoginException;
import com.aljjabaegi.api.config.security.jwt.record.TokenResponse;
import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
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
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    protected static final long ACCESS_EXPIRATION_MILLISECONDS = 24 * (1000 * 3600); //24시간
    protected static final long REFRESH_EXPIRATION_MILLISECONDS = (24 * 7) * (1000 * 3600); //일주일

    private final MemberRepository memberRepository;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final String jwtCookieName = "AJP_AUT";
    private final String authorityClaimName = "AJP_ATC";
    private JwtParser jwtParser;
    private SecretKey secretKey;

    @Value("${security.jwt.auth-key}")
    private String authoritiesKey;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.authoritiesKey));
        this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
    }

    /**
     * Generate token
     *
     * @param authentication security Authentication
     * @param milliseconds   만료 초
     * @param entity         member entity
     * @return 생성된 token
     * @author GEONLEE
     * @since 2024-04-02<br />
     * 2024-04-15 GEONLEE - member entity parameter 추가
     */
    public String generateToken(Authentication authentication, long milliseconds, Member entity) {
        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + milliseconds);
        return Jwts.builder()
                .subject(authentication.getName())
                .issuer(this.issuer)
                .signWith(this.secretKey)
                .claim(this.authorityClaimName, authority)
                .claim("name", entity.getMemberName())
                .expiration(validity)
                .compact();
    }


    /**
     * Generate Access and refresh token
     *
     * @param authentication   Security 인증정보
     * @param entity           member entity
     * @param isChangePassword 비밀번호 변경 여부
     * @return TokenResponse 토큰 응답 record
     * @author GEONLEE
     * @since 2022-11-11<br />
     * 2024-04-15 GEONLEE - member entity parameter 추가<br />
     * 2024-04-17 GEONLEE - isChangeParameter 추가<br />
     */
    public TokenResponse generateTokenResponse(Authentication authentication, Member entity, boolean isChangePassword) {
        String tokenType = "Bearer";
        return TokenResponse.builder()
                .token(generateToken(authentication, ACCESS_EXPIRATION_MILLISECONDS, entity))
                .refreshToken(generateToken(authentication, REFRESH_EXPIRATION_MILLISECONDS, entity))
                .tokenType(tokenType)
                .expirationSeconds(ACCESS_EXPIRATION_MILLISECONDS)
                .isChangePassword(isChangePassword)
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
     * token 에서 Name 추출
     *
     * @param token token 값
     * @return user name
     * @author GEONLEE
     * @since 2024-04-15
     */
    public String getNameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return String.valueOf(claims.get("name"));
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
        grantedAuthorities.add(new SimpleGrantedAuthority((String) claims.get(this.authorityClaimName)));
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
     * Cookie 의 토큰을 만료시킨다.<br />
     * 로그아웃 시 활용
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
        if (ObjectUtils.isEmpty(cookies)) {
            return null;
        }
        Optional<String> optionalAccessToken = Arrays.stream(cookies)
                .filter(cookie -> jwtCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
        if (optionalAccessToken.isPresent()) {
            return doXssFilter(optionalAccessToken.get());
        } else {
            String requestURI = httpServletRequest.getRequestURI().replace(this.contextPath, "");
            log.error("Access token in cookie does not exist. request URI: {}", requestURI);
            return null;
        }
    }

    public String getRefreshTokenFromDatabase(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = this.getTokenFromCookie(httpServletRequest);
        Optional<Member> manager = memberRepository.findByAccessToken(accessToken);
        if (manager.isPresent() && StringUtils.hasText(manager.get().getAccessToken())) {
            return doXssFilter(manager.get().getRefreshToken());
        } else {
            if (!memberRepository.existsByAccessToken(accessToken)) {
                log.error("Duplicated login. Access token does not exist in the database. access token: {}", accessToken);
                this.jwtAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse
                        , new DuplicateLoginException("Duplicate login detection."));
            }
        }
        return null;
    }

    public boolean validateToken(TokenType tokenType, String token) {
        try {
            this.jwtParser.parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid jwt signature.");
        } catch (ExpiredJwtException e) {
            log.error("{} token is expired.", tokenType);
        } catch (UnsupportedJwtException e) {
            log.info("This jwt token is not supported.");
        } catch (IllegalArgumentException e) {
            log.info("Invalid jwt token.");
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
