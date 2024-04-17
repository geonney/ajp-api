package com.aljjabaegi.api.domain.login;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.config.security.jwt.TokenProvider;
import com.aljjabaegi.api.config.security.jwt.record.TokenResponse;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import com.aljjabaegi.api.domain.historyLogin.HistoryLoginMapper;
import com.aljjabaegi.api.domain.historyLogin.HistoryLoginRepository;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginCreateRequest;
import com.aljjabaegi.api.domain.login.record.LoginRequest;
import com.aljjabaegi.api.domain.login.record.LogoutResponse;
import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.entity.Member;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * login, logout service
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 * 2024-04-03 GEONLEE - RSA 복호화 코드 추가<br />
 * 2024-04-15 GEONLEE - generateTokenResponse 응답 생성 시 member entity parameter 추가<br />
 * 2024-04-17 GEONLEE - 비밀번호 변경 주기 로직 추가<br />
 */
@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final HistoryLoginRepository historyLoginRepository;
    private final HistoryLoginMapper historyLoginMapper = HistoryLoginMapper.INSTANCE;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RsaProvider rsaProvider;

    @Value("${security.password.cycle}")
    int passwordUpdateCycle;

    @Transactional
    public ResponseEntity<ItemResponse<TokenResponse>> login(
            LoginRequest parameter, HttpServletResponse httpServletResponse) throws ServiceException {
        // 1. ID가 존재하는지 체크
        Member entity = memberRepository.findById(parameter.id())
                .orElseThrow(() -> new ServiceException(CommonErrorCode.ID_NOT_FOUND));
        // 2. Password 가 일치하는지 체크
        String encodePassword = rsaProvider.decrypt(parameter.password());
        if (!passwordEncoder.matches(encodePassword, entity.getPassword())) {
            throw new ServiceException(CommonErrorCode.WRONG_PASSWORD);
        }
        // 3. 사용자 권한 체크
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                parameter.id(), encodePassword);
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4. 패스워드 변경 주기 체크
        boolean isChangePassword = LocalDate.now().isAfter(entity.getPasswordUpdateDate().plusDays(passwordUpdateCycle));
        // 5. JWT 토큰 응답 생성
        TokenResponse tokenResponse = tokenProvider.generateTokenResponse(authentication, entity, isChangePassword);
        // 6. 로그인 성공 시 DB Token 정보 갱신
        entity.setAccessToken(tokenResponse.token());
        entity.setRefreshToken(tokenResponse.refreshToken());
        // 7. 쿠키에 Access Token 추가
        tokenProvider.renewalAccessTokenInCookie(httpServletResponse, tokenResponse.token());
        // 8. 로그인 이력 저장
        /*
         * 키 or 복합키에 @EnableJpaAuditing annotation 을 사용할 경우 동작하지 않음.
         * 키인 경우에는 직접 값을 입력하여 처리
         * */
        historyLoginRepository.saveAndFlush(
                historyLoginMapper.toEntity(
                        HistoryLoginCreateRequest.builder()
                                .memberId(parameter.id())
                                .createDate(LocalDateTime.now())
                                .loginIp("127.0.0.1")
                                .build()));
        return ResponseEntity.ok()
                .body(ItemResponse.<TokenResponse>builder()
                        .status("OK")
                        .message("로그인에 성공하였습니다.")
                        .item(tokenResponse)
                        .build());
    }

    @Transactional
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = tokenProvider.getTokenFromCookie(httpServletRequest);
        if (StringUtils.hasText(accessToken)) {
            try {
                String userId = tokenProvider.getIdFromToken(accessToken);
                // 토큰 만료 처리
                tokenProvider.expirationToken(httpServletResponse);
                // DB Access, Refresh Token 초기화
                memberRepository.findById(userId).ifPresent(member -> {
                    member.setAccessToken(null);
                    member.setRefreshToken(null);
                });
            } catch (JwtException e) {
                throw new ServiceException(CommonErrorCode.EXPIRED_TOKEN, e);
            } catch (PersistenceException e) {
                throw new ServiceException(CommonErrorCode.SERVICE_ERROR, e);
            }
        } else {
            throw new ServiceException(CommonErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok()
                .body(LogoutResponse.builder()
                        .status("OK")
                        .message("로그아웃 하였습니다.")
                        .build());
    }
}
