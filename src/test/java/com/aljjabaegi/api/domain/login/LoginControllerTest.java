package com.aljjabaegi.api.domain.login;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.response.ErrorResponse;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.domain.login.record.LoginRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author GEONLEE
 * @since 2024-04-26
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@SpringBootTest
//@AutoConfigureMockMvc
//@WebMvcTest(LoginController.class)
@TestPropertySource(locations = "classpath:/application.yml")
@ActiveProfiles
class LoginControllerTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인 실패 - 패스워드 다름")
    void login() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = new LoginRequest("honggildong123", "s");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andReturn();
        ResponseEntity<ItemResponse<ErrorResponse>> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<ResponseEntity<ItemResponse<ErrorResponse>>>() {
                });

        ErrorResponse errorResponse = Objects.requireNonNull(response.getBody()).item();
        Assertions.assertEquals(CommonErrorCode.UNAUTHORIZED.status(), errorResponse.status());
    }

    @Test
    void logout() {
    }
}