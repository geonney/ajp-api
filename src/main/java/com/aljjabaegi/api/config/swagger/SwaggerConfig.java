package com.aljjabaegi.api.config.swagger;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.code.ErrorCode;
import com.aljjabaegi.api.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
 * @since 2024-04-01
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Aljjabaegi Programmer API Documentation",
                description = """
                        - <a href="https://aljjabaegi.tistory.com/search/swagger" target="_blank">Swagger</a>
                        """, version = "v1.0.0"),
        servers = @Server(url = "/ajp-api") //ip:port 까지 입력할 경우 CORS 발생
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
        responses.put("500", serverError());
        return responses;
    }

    /**
     * 400 - 클라이언트 요청 실패 관련 Response 리턴 메서드
     *
     * @author GEONLEE
     * @since 2024-03-19<br />
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
     * @since 2024-03-19<br />
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
