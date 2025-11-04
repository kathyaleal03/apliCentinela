package com.example.centinela_api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomRequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomRequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequest(wrappedRequest);
            // copy response body back to the original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        try {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String client = request.getRemoteAddr();

            byte[] buf = request.getContentAsByteArray();
            String payload = "";
            if (buf != null && buf.length > 0) {
                String charset = request.getCharacterEncoding() != null ? request.getCharacterEncoding() : StandardCharsets.UTF_8.name();
                payload = new String(buf, charset);
                // mask password fields to avoid leaking secrets
                payload = payload.replaceAll("(\"contrasena\"\s*:\s*)\".*?\"", "$1\"***masked***\"");
            }

            StringBuilder msg = new StringBuilder();
            msg.append("REQUEST DATA : method=").append(method)
                    .append(" uri=").append(uri)
                    .append(query != null ? "?" + query : "")
                    .append(" client=").append(client)
                    .append(" payload=").append(payload);

            logger.info(msg.toString());
        } catch (Exception ex) {
            logger.warn("Failed to log request payload", ex);
        }
    }
}
