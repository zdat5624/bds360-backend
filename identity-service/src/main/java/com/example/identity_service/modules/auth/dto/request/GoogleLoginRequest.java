// auth/dto/request/GoogleLoginRequest.java
package com.example.identity_service.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    @NotBlank(message = "Token không được để trống")
    private String token;
}