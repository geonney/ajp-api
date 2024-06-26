package com.aljjabaegi.api.common.exception.handler;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.code.ErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLDataException;
import java.util.List;
import java.util.StringJoiner;

/**
 * 전역 Exception 처리
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 * 2024-04-03 GEONLEE - 에러 로그 표출 수정 message -> enum<br />
 * 2024-04-05 GEONLEE - Unchecked Exception 에 RuntimeException 추가<br />
 * 2024-04-11 GEONLEE - handleJsonParseException 추가<br />
 * 2024-04-24 GEONLEE - Security filter 에서 권한 오류 발생 위해 RuntimeException 제거<br />
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.profiles.active}")
    private String active;

    /**
     * Checked Exception 관련 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler(value = {ServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //추가 시 Swagger Response 에 등록됨.
    public ResponseEntity<ErrorResponse> handleCheckedException(ServiceException e) {
        return handleExceptionInternal(e.errorCode, e);
    }

    /**
     * Unchecked Exception 관련 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler(value = {PSQLException.class, IOException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleUncheckedException(Exception e) {
        CommonErrorCode errorCode = CommonErrorCode.SERVICE_ERROR;
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * 이미 존재하는 Entity 일 경우 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler(value = {EntityExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException e) {
        CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * requestBody 처리 시 발생하는 Json parsing 에러 처리 (요청 구조체 오류)
     *
     * @author GEONLEE
     * @since 2024-04-11
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleJsonParseException(JsonParseException e, HttpServletRequest httpServletRequest) {
        CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        printRequestPayload(httpServletRequest);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * 데이터 포멧과 맞지 않거나 범위를 벗어난 값 전달 시 처리
     *
     * @author GEONLEE
     * @since 2024-05-29
     */
    @ExceptionHandler(value = SQLDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleSQLDataException(SQLDataException e) {
        CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * 데이터 없음 관련 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        CommonErrorCode errorCode = CommonErrorCode.ENTITY_NOT_FOUND;
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * Validation 관련 Exception 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        StringJoiner stringJoiner = new StringJoiner(", ");
        LOGGER.error("======================@Valid Exception START======================");
        LOGGER.error("object : {}", e.getBindingResult().getObjectName());
        List<FieldError> fieldList = e.getFieldErrors();
        for (FieldError field : fieldList) {
            stringJoiner.add(field.getField() + ": " + field.getDefaultMessage());
        }
        stringBuilder.append(stringJoiner);
        LOGGER.error(stringBuilder.toString());
        LOGGER.error("======================@Valid Exception End========================");
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, new InvalidParameterException(stringJoiner.toString()));
    }

    /**
     * Custom Validation 관련 Exception 처리 (필수 파라미터))
     *
     * @author GEONLEE
     * @since 2024-06-26
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        e.getConstraintViolations().forEach(constraintViolation -> {
            constraintViolation.getPropertyPath().forEach(node -> {
                if (node.getKind().name().equals("PROPERTY")) {
                    stringJoiner.add(node.getName());
                }
            });
        });
        ErrorCode errorCode = CommonErrorCode.REQUIRED_PARAMETER;
        return handleExceptionInternal(errorCode, new ServiceException(CommonErrorCode.REQUIRED_PARAMETER, stringJoiner.toString()));
    }

    /**
     * ErrorResponse return method
     *
     * @author GEONLEE
     * @since 2024-04-11<br />
     * 2024-05-21 GEONLEE ErrorResponse detailMessage 추가<br />
     * 2024-05-24 GEONLEE - 개발 환경에서만 detailMessage 전송되도록 수정<br />
     */
    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, Exception e) {
        /* 모든 HTTP Status 코드는 200으로 전달하고 내부 코드를 상세히 전달 */
        String detailMessage = null;
        LOGGER.error("[" + errorCode.status() + "] {}", errorCode, e);
        if ("dev".equals(this.active)) {
            detailMessage = (errorCode.message().equals(e.getMessage())) ? null : e.getMessage();
        }
        return ResponseEntity.ok()
                .header("Content-type", String.valueOf(MediaType.APPLICATION_JSON))
                .body(ErrorResponse.builder()
                        .status(errorCode.status())
                        .message(errorCode.message())
                        .detailMessage(detailMessage)
                        .build());
    }

    /**
     * Method to logging invalid json format<br />
     * RequestFilter, CustomRequestWrapper 필요<br />
     *
     * @author GEONLEE
     * @since 2024-04-11
     */
    private void printRequestPayload(HttpServletRequest httpServletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = httpServletRequest.getReader()) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY);
        }
        LOGGER.error("Invalid Json format request -> " + stringBuilder.toString());
    }
}
