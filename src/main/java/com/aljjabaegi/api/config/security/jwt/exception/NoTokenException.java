package egovframework.kt.dxp.api.config.jwt.exception;

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
