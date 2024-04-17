package com.aljjabaegi.api.config.security.springSecurity;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring error controller implementation
 *
 * @author GEONLEE
 * @since 2024-04-17
 */
@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping(value = "/error")
    public ResponseEntity<ErrorResponse> error() {
        throw new ServiceException(CommonErrorCode.INVALID_PARAMETER);
    }
}
