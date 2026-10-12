package com.example.payment_service.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.payment_service.common.dto.response.ApiResponse;
import com.example.payment_service.common.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.error(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage());

        System.out.println("Log lỗi ở CustomAuthenticationEntryPoint implements AuthenticationEntryPoint");

        mapper.writeValue(response.getWriter(), apiResponse);
    }
}