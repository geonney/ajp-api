package com.aljjabaegi.api.common.exception.custom;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;

import java.io.Serial;

/**
 * RuntimeException 처리용 Exception, Checked Exception
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public final CommonErrorCode errorCode;


    public ServiceException(CommonErrorCode errorCode, Throwable cause) {
        super(errorCode.message(), cause);
        this.errorCode = errorCode;
    }

    public ServiceException(CommonErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
