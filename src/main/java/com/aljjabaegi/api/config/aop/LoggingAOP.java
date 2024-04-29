package com.aljjabaegi.api.config.aop;

import com.aljjabaegi.api.common.response.GridResponse;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

/**
 * HTTP method 별 AOP 로 동작하는 로깅 클래스
 * resources/logback/logback-spring.xml 에서 설정하며, 파일 로깅한다.
 *
 * @author GEON LEE
 * @since 2024-04-16
 */
@Aspect
@Component
public class LoggingAOP {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAOP.class);

    /**
     * Around logging AOP<br />
     * Except HttpServletResponseWrapper
     */
    @Before("execution(public * com.aljjabaegi.api.domain.*.*Controller.*(..))")
    public void loggingBefore(JoinPoint joinPoint) {
        loggingRequest();
        Object[] arguments = joinPoint.getArgs();
        Arrays.stream(arguments)
                .filter(Objects::nonNull)
                .filter(arg -> !(arg instanceof HttpServletResponseWrapper))
                .forEach(this::loggingParameter);
    }

    /**
     * Request 정보 로깅 method, URI
     */
    private void loggingRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();
        LOGGER.info("[Request][{}] : {}", request.getMethod(), requestUri);
    }

    /**
     * Request parameter logging
     */
    private void loggingParameter(Object objects) {
        LOGGER.info("[Parameter] " + new Gson().toJson(objects));
    }

    /**
     * Logging Success Response
     *
     * @author GEON LEE
     * @since 2024-04-17<br />
     */
    @AfterReturning(pointcut = "execution(public * com.aljjabaegi.api.domain.*.*Controller.*(..))", returning = "responseEntity")
    public void loggingAfterReturning(ResponseEntity<?> responseEntity) {
        LOGGER.info("[Response][Success]");
        Class<?> clazz = Objects.requireNonNull(responseEntity.getBody()).getClass();
        Object body = responseEntity.getBody();
        switch (clazz.getSimpleName()) {
            case "ItemResponse" -> {
                ItemResponse<?> itemResponse = (ItemResponse<?>) body;
                LOGGER.info("[Body][ItemResponse][{}]{}\n", itemResponse.item().getClass().getSimpleName(), new Gson().toJson(itemResponse.item()));
            }
            case "ItemsResponse" -> {
                ItemsResponse<?> itemsResponse = (ItemsResponse<?>) body;
                LOGGER.info("[Body][ItemsResponse] size: {}\n", itemsResponse.totalSize());
            }
            case "GridItemsResponse" -> {
                GridResponse<?> gridItemsResponse = (GridResponse<?>) body;
                Long totalSize = gridItemsResponse.totalSize();
                int totalPageSize = gridItemsResponse.totalPageSize();
                int size = gridItemsResponse.size();
                LOGGER.info("[Body][GridItemsResponse] totalSize: {}, size: {}, totalPageSize: {}\n", totalSize, size, totalPageSize);
            }
        }
    }

    /**
     * Logging Failure response
     */
    @AfterThrowing(pointcut = "execution(public * com.aljjabaegi.api.domain.*.*Controller.*(..))", throwing = "e")
    public void loggingAfterThrowing(Throwable e) {
        LOGGER.info("[Response][Failure] Exception: {}, message: {}\n", e.getClass().getSimpleName(), e.getMessage());
    }
}
