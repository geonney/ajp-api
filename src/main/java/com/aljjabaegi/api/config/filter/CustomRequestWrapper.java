package com.aljjabaegi.api.config.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * request.getReader() 를 사용하게되면 request body 를 읽기위한 스트림을 반환하고<br />
 * 읽는 동안 내부적으로 포인트를 사용하여 읽을 위치를 기억하게 됨.<br />
 * 처음 다 읽은 후 두번 째 읽을 때는 이미 포인터가 body 의 마지막 부분을 기억하고 있기 떄문에 읽을 데이터가<br />
 * 없다고 판단. Filter 에 CustomRequestWrapper 를 추가하여 해당 URI 로 요청이 오면 처음 읽을<br />
 * 때 저장된 InputStream 을 복사하여 전달하도록 구현<br />
 *
 * @author GEONLEE
 * @since 2024-04-11
 */
public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private final Charset encoding;
    private final byte[] rawData;

    public CustomRequestWrapper(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);

        String characterEncoding = httpServletRequest.getCharacterEncoding();
        if (StringUtils.isBlank(characterEncoding)) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(characterEncoding);

        try (InputStream inputStream = httpServletRequest.getInputStream()) {
            this.rawData = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }
}
