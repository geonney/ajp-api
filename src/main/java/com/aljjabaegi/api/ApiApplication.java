package com.aljjabaegi.api;

import com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.repository.JpaDynamicDslRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Project main class
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-01 GEONLEE - @MappedSuperclass 사용으로 @EnableJpaAuditing 추가<br />
 * 2024-04-18 GEONLEE - @EnableJpaRepositories 추가<br />
 * 2024-04-24 GEONLEE - EnableMethodSecurity 권한 처리 추가<br />
 * - securedEnabled(default false) @Secured({"ROLE_ADMIN", "ROLE_TEST"}) 사용여부<br />
 * - prePostEnabled(default true) @PreAuthorize, @PostAuthorize 사용 여부<br />
 */
@EnableJpaAuditing
@EntityScan(basePackages = "com.aljjabaegi.api.entity")
@EnableJpaRepositories(repositoryBaseClass = JpaDynamicDslRepositoryImpl.class)
@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true)
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
