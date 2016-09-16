package org.tkalenko.spring.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

public class TokenSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
        response.setContentLength(message.length());
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(message);
    }
}
