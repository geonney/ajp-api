package com.aljjabaegi.api.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * GlobalExceptionHandler 등 에서<br >
 * httpServletRequest.getReader() 를 다시 호출하기 위해 HttpServletRequestWrapper 를 등록 하는 Filter
 *
 * @author GEONLEE
 * @since 2024-04-11
 */
public class RequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        CustomRequestWrapper reReadableRequestWrapper = new CustomRequestWrapper((HttpServletRequest) request);
        chain.doFilter(reReadableRequestWrapper, response);
    }
}
