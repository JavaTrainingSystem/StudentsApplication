package com.tcs.students.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.students.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private List<String> skipEndpoints;


    @Value("${skip.filter.endpoints}")
    public void setSkipEndpoints(String skipEndpoints) {
        this.skipEndpoints = Arrays.asList(skipEndpoints.split(","));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api")) {

            if (skipEndpoints.stream().noneMatch(endPoint -> endPoint.equalsIgnoreCase(request.getRequestURI()))) {

                final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (!jwtUtils.validateToken(token)) {
                    Map<String, Object> unAuthorizedResponse = new HashMap<>();
                    unAuthorizedResponse.put("message", "UnAuthorized");
                    unAuthorizedResponse.put("status", "FAILED");

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                    objectMapper.writeValue(response.getWriter(), unAuthorizedResponse);
                    return;
                }
            }

        }

        filterChain.doFilter(request, response);
    }

}
