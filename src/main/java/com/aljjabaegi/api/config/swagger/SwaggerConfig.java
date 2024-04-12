package com.aljjabaegi.api.config.swagger;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.code.ErrorCode;
import com.aljjabaegi.api.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Swagger setting
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-02 GEONLEE - apply JWT Authentication<br />
 * 2024-04-12 GEONLEE - License, contact 추가<br />
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Aljjabaegi Programmer API Documentation",
                description = """
                        - <a href="https://github.com/aljjabaegiProgrammer" target="_blank">Git</a>
                        - <a href="https://aljjabaegi.tistory.com" target="_blank">Blog</a>
                        """,
                version = "v1.0.0",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(url = "https://aljjabaegi.tistory.com", name = "GEON LEE", email = "geonlee@kakao.com")
        ),
        servers = @Server(url = "/ajp-api") //ip:port 까지 입력할 경우 CORS 발생
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi version1APi() {
        return GroupedOpenApi.builder()
                .group("v1.0")
                .pathsToMatch("/v1/**")
                .addOperationCustomizer(operationCustomizer())
                .build();
    }

    /**
     * Operation 의 response 를 커스텀
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            ApiResponses apiResponses = operation.getResponses();
            if (apiResponses == null) {
                apiResponses = new ApiResponses();
                operation.setResponses(apiResponses);
            }
            apiResponses.putAll(generateDefaultResponses(handlerMethod));
            return operation;
        };
    }

    /**
     * API 공통 response 를 생성
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    private Map<String, ApiResponse> generateDefaultResponses(HandlerMethod handlerMethod) {
        LinkedHashMap<String, ApiResponse> responses = new LinkedHashMap<>();
        responses.put("400", clientError());
        responses.put("401", authenticationError());
        responses.put("500", serverError());
        return responses;
    }

    /**
     * 400 - 클라이언트 요청 실패 관련 Response 리턴 메서드
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     */
    private ApiResponse clientError() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("""
                Bad Request
                - 요청한 정보가 올바른지 확인한다.
                """);
        addContent(apiResponse, CommonErrorCode.INVALID_PARAMETER);
        return apiResponse;
    }

    /**
     * 401 - 인증 실패 관련 Response 리턴 메서드
     *
     * @author GEONLEE
     * @since 2024-04-03<br />
     */
    private ApiResponse authenticationError() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("""
                UnAuthorized
                - 인증 정보가 올바른지 확인한다. (or 중복 로그인)
                """);
        addContent(apiResponse, CommonErrorCode.UNAUTHORIZED);
        return apiResponse;
    }

    /**
     * 500 - 서버 에러 관련 Response 리턴 메서드
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     */
    private ApiResponse serverError() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("""
                Internal Server Error
                - 서버 에러 로그 확인을 요청한다.
                """);
        addContent(apiResponse, CommonErrorCode.SERVICE_ERROR);
        return apiResponse;
    }

    /**
     * response 에 content 를 추가 메서드
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     */
    @SuppressWarnings("rawtypes")
    private void addContent(ApiResponse apiResponse, ErrorCode errorCode) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        Schema schema = new Schema<>();
        schema.$ref("#/components/schemas/ErrorResponse");
        mediaType.schema(schema).example(ErrorResponse.builder()
                .status(errorCode.status())
                .message(errorCode.message())
                .build());
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
    }
}
