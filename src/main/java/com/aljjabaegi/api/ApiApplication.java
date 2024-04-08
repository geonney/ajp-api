package com.aljjabaegi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

/**
 * Project main class
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-01 GEONLEE - @MappedSuperclass 사용으로 @EnableJpaAuditing 추가<br />
 * 2024-04-08 GEONLEE - Swagger 주소 출력 로그 추가<br />
 */
@SpringBootApplication
@EnableJpaAuditing
public class ApiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Component
    public class SwaggerInformation implements ApplicationRunner {
        @Value("${server.port}")
        private String port;
        @Value("${server.servlet.context-path}")
        private String contextPath;

        @Override
        public void run(ApplicationArguments args) {
            LOGGER.info("Swagger : http://localhost:" + port + contextPath + "/swagger-ui/index.html");
        }
    }

}
