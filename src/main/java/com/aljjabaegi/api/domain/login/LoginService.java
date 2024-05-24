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
import com.aljjabaegi.api.entity.HistoryLogin;
import com.aljjabaegi.api.entity.Member;
import com.aljjabaegi.api.entity.enumerated.UseYn;
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
 * 2024-04-26 GEONLEE - 로그인 시도 횟수 처리 추가에 따른 login method 에 @Transactional 제거<br />
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
    private int passwordUpdateCycle;

    @Value("${security.password.locked}")
    private int locked;

    public ResponseEntity<ItemResponse<TokenResponse>> login(
            LoginRequest parameter, HttpServletResponse httpServletResponse) throws ServiceException {
        // 1. ID가 존재하는지 체크
        Member entity = memberRepository.findById(parameter.id())
                .orElseThrow(() -> new ServiceException(CommonErrorCode.ID_NOT_FOUND));
        // 2. 잠긴 회원 체크
        if (UseYn.N == entity.getUseYn()) {
            throw new ServiceException(CommonErrorCode.LOCKED_MEMBER, "'" + entity.getMemberId() + "' is a locked member.");
        }
        // 3. Password 가 일치하는지 체크
        String encodePassword = "";
        try {
            encodePassword = rsaProvider.decrypt(parameter.password());
            if (!passwordEncoder.matches(encodePassword, entity.getPassword())) {
                throw new ServiceException(CommonErrorCode.WRONG_PASSWORD);
            }
        } catch (ServiceException e) {
            entity.setLoginAttemptsCount((entity.getLoginAttemptsCount() == null) ? 1 : entity.getLoginAttemptsCount() + 1);
            if (entity.getLoginAttemptsCount() == this.locked) {
                entity.setUseYn(UseYn.N);
            }
            memberRepository.save(entity);
            throw new ServiceException(CommonErrorCode.WRONG_PASSWORD);
        }
        // 4. 사용자 권한 체크
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                parameter.id(), encodePassword);
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 5. 패스워드 변경 주기 체크
        boolean isChangePassword = LocalDate.now().isAfter(entity.getPasswordUpdateDate().plusDays(passwordUpdateCycle));
        // 6. JWT 토큰 응답 생성
        TokenResponse tokenResponse = tokenProvider.generateTokenResponse(authentication, entity, isChangePassword);
        // 7. 로그인 성공 시 DB Token 정보 갱신, 로그인 시도 0으로 초기화
        entity.setAccessToken(tokenResponse.token());
        entity.setRefreshToken(tokenResponse.refreshToken());
        entity.setLoginAttemptsCount(0);
        memberRepository.save(entity);
        // 8. 쿠키에 Access Token 추가
        tokenProvider.renewalAccessTokenInCookie(httpServletResponse, tokenResponse.token());
        // 9. 로그인 이력 저장
        /*
         * 키 or 복합키에 @EnableJpaAuditing annotation 을 사용할 경우 동작하지 않음.
         * 키인 경우에는 직접 값을 입력하여 처리
         * */
        HistoryLogin historyLogin = historyLoginMapper.toEntity(
                HistoryLoginCreateRequest.builder()
                        .memberId(parameter.id())
                        .createDate(LocalDateTime.now())
                        .loginIp("127.0.0.1")
                        .build());
        historyLogin.setMember(entity);
        historyLoginRepository.save(historyLogin);
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
