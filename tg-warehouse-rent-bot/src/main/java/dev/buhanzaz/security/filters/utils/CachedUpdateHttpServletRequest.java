package dev.buhanzaz.security.filters.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class CachedUpdateHttpServletRequest extends HttpServletRequestWrapper {
    private final byte[] body;

    public CachedUpdateHttpServletRequest(HttpServletRequest request, byte[] body) throws IOException {
        super(request);
        this.body = body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() {
        var bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
