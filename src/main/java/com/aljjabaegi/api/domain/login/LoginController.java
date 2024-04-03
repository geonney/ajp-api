package com.aljjabaegi.api.domain.login;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.config.security.jwt.record.TokenResponse;
import com.aljjabaegi.api.domain.login.record.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "로그인 / 로그아웃", description = "담당자: GEONLEE")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping(value = "/v1/login")
    @Operation(summary = "사용자 로그인", operationId = "API-LOGIN")
    public ResponseEntity<ItemResponse<TokenResponse>> createUser(
            @RequestBody @Valid LoginRequest parameter, HttpServletResponse httpServletResponse) {
        return loginService.login(parameter, httpServletResponse);
    }
}
