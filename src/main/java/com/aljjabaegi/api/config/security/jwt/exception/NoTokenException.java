package com.aljjabaegi.api.config.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author GEONLEE
 * @since 2024-12-13
 */
public class NoTokenException extends AuthenticationException {
    public NoTokenException(String msg) {
        super(msg);
    }
}
