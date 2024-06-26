package com.aljjabaegi.api.config.security.springSecurity;

import com.aljjabaegi.api.config.filter.RequestFilter;
import com.aljjabaegi.api.config.security.jwt.JwtAccessDeniedHandler;
import com.aljjabaegi.api.config.security.jwt.JwtAuthenticationEntryPoint;
import com.aljjabaegi.api.config.security.jwt.JwtFilter;
import com.aljjabaegi.api.config.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * 인증처리와 401,403 에러 처리, 암호화 Security Filter
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 * 2024-04-03 GEONLEE - 2가지 passwordEncoder test<br />
 * 2024-04-11 GEONLEE - RequestFilter 추가<br />
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    public static final String[] SWAGGER_URIS = {"swagger-ui.html", "/swagger-ui/**", "/api-docs/**"};
    public static final String[] IGNORE_URIS = {"/v1/login", "/v1/public-key", "/favicon.ico", "/error"};
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * PasswordEncoder 설정<br />
     * 참고 PasswordEncoderFactories.createDelegatingPasswordEncoder()<br />
     * key 만 변경하면 암호화 적용
     *
     * @author GEONLEE
     * @since 2024-04-03
     */
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        return new DelegatingPasswordEncoder("SHA-256", encoders);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(IGNORE_URIS).permitAll()
                            .requestMatchers(SWAGGER_URIS).permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c ->
                        c.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler))
//                .apply(new JwtSecurityConfig(tokenProvider, messageConfig)); /*spring 6.2 deprecated*/
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
//                .addFilterAfter(new RequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}