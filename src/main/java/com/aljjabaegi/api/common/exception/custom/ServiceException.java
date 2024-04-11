package com.aljjabaegi.api.common.exception.custom;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;

import java.io.Serial;

/**
 * RuntimeException 처리용 Exception, Checked Exception
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 * 2024-04-11 GEONLEE - apply ServiceException(CommonErrorCode errorCode, String message)<br />
 * For custom message logging<br />
 */
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public final CommonErrorCode errorCode;

    /**
     * CommonErrorCode 와 Exception 이 전달된 경우<br />
     * RuntimeException 으로 message 와 Exception 전달
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    public ServiceException(CommonErrorCode errorCode, Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * CommonErrorCode, custom Message 가 전달된 경우<br />
     *
     * @author GEONLEE
     * @since 2024-04-11
     */
    public ServiceException(CommonErrorCode errorCode, String message) {
        super(message, null);
        this.errorCode = errorCode;
    }

    /**
     * CommonErrorCode 만 전달된 경우<br />
     *
     * @author GEONLEE
     * @since 2024-04-02<br />
     * RuntimeException 으로 message 전달 추가
     */
    public ServiceException(CommonErrorCode errorCode) {
        super(errorCode.message(), null);
        this.errorCode = errorCode;
    }
}
