package com.aljjabaegi.api;

import com.aljjabaegi.api.common.jpa.dynamicSearch.specification.repository.JpaDynamicRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Project main class
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-01 GEONLEE - @MappedSuperclass 사용으로 @EnableJpaAuditing 추가<br />
 * 2024-04-18 GEONLEE - @EnableJpaRepositories 추가<br />
 */
@EnableJpaAuditing
@EntityScan(basePackages = "com.aljjabaegi.api.entity")
@EnableJpaRepositories(repositoryBaseClass = JpaDynamicRepositoryImpl.class)
@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
