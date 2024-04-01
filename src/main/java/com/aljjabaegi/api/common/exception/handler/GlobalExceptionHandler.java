package com.aljjabaegi.api.common.exception.handler;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.code.ErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.response.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.StringJoiner;

/**
 * 전역 Exception 처리
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
     * 이미 존재하는 Entity 일 경우 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler(value = {EntityExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(Exception e) {
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
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Exception e) {
        CommonErrorCode errorCode = CommonErrorCode.ENTITY_NOT_FOUND;
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * Validation 관련 Exception 처리
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        StringJoiner stringJoiner = new StringJoiner(",");
        LOGGER.error("======================@Valid Exception START======================");
        LOGGER.error("object : {}", e.getBindingResult().getObjectName());
        List<FieldError> fieldList = e.getFieldErrors();
        for (FieldError field : fieldList) {
            stringJoiner.add("{field: " + field.getField() + ", message: " + field.getDefaultMessage() + "}");
        }
        stringBuilder.append(stringJoiner);
        LOGGER.error(stringBuilder.toString());
        LOGGER.error("======================@Valid Exception End========================");
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, Exception e) {
        /* 모든 HTTP Status 코드는 200으로 전달하고 내부 코드를 상세히 전달 */
        LOGGER.error("[" + errorCode.status() + "] {}", errorCode.message(), e);
        return ResponseEntity.ok()
                .header("Content-type", String.valueOf(MediaType.APPLICATION_JSON))
                .body(new ErrorResponse(errorCode.status(), errorCode.message()));
    }
}
