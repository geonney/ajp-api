package com.aljjabaegi.api.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .build();
    }
}
