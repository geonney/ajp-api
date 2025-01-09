package egovframework.kt.dxp.api.config.jwt.exception;

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
