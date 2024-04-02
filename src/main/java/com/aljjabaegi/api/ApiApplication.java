package com.aljjabaegi.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Project main class
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-01 GEONLEE - @MappedSuperclass 사용으로 @EnableJpaAuditing 추가
 */
@SpringBootApplication
@EnableJpaAuditing
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
