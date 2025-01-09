package com.aljjabaegi.api.config.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author GEONLEE
 * @since 2024-12-12
 */
public class DuplicateLoginException extends AuthenticationException {

    public DuplicateLoginException(String msg) {
        super(msg);
    }
}
