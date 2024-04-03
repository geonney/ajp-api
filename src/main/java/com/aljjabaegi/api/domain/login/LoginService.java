package com.aljjabaegi.api.domain.login;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.config.security.jwt.TokenProvider;
import com.aljjabaegi.api.config.security.jwt.record.TokenResponse;
import com.aljjabaegi.api.domain.login.record.LoginRequest;
import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * login service
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<ItemResponse<TokenResponse>> login(
            LoginRequest parameter, HttpServletResponse httpServletResponse) throws ServiceException {
        //1. ID가 존재하는지 체크
        Member entity = memberRepository.findById(parameter.id())
                .orElseThrow(() -> new EntityNotFoundException(parameter.id()));
        //2. Password 가 일치하는지 체크
        if (!passwordEncoder.matches(parameter.password(), entity.getPassword())) {
            throw new ServiceException(CommonErrorCode.WRONG_PASSWORD, null);
        }
        //3. 사용자 권한 체크
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                parameter.id(), parameter.password());
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //4. JWT 토큰 생성
        TokenResponse tokenResponse = tokenProvider.generateTokenResponse(authentication);
        //5. 로그인 성공 시 DB Token 정보 갱신
        entity.setAccessToken(tokenResponse.token());
        entity.setRefreshToken(tokenResponse.refreshToken());
        //7. 쿠키에 Access Token 추가
        tokenProvider.renewalAccessTokenInCookie(httpServletResponse, tokenResponse.token());

        return ResponseEntity.ok()
                .body(ItemResponse.<TokenResponse>builder()
                        .status("OK")
                        .message("로그인에 성공하였습니다.")
                        .item(tokenResponse)
                        .build());
    }
}
