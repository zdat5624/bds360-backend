package com.example.identity_service.modules.auth.dto.response;

import com.example.identity_service.modules.user.dto.response.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private UserResponse user;
}