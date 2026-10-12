package com.example.identity_service.modules.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity_service.common.dto.response.ApiResponse;
import com.example.identity_service.security.annotation.CurrentUser;
import com.example.identity_service.security.annotation.RequireLogin;
import com.example.identity_service.security.dto.UserContext;

@RestController
@RequestMapping("/api/v1/users")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        String t = "Identity Service is running!  33";
        return t;
    }

    @GetMapping("/ping1")
    @RequireLogin
    public ApiResponse<UserContext> testCurrentUser(@CurrentUser UserContext userContext) {
        // Nếu "phép thuật" hoạt động, nó sẽ in ra cục JSON chứa đủ ID, Email, Name,
        // Phone

        System.out.println(">>> User Context: " + userContext.getId() + ", " + userContext.getEmail() + ", "
                + userContext.getName() + ", "
                + userContext.getPhone() + ", " + userContext.getRole());
        return ApiResponse.success(userContext,
                "Phép thuật Argument Resolver hoạt động hoàn hảo! User Context: " + userContext.getId() + ", "
                        + userContext.getEmail()
                        + ", "
                        + userContext.getName() + ", "
                        + userContext.getPhone() + ", " + userContext.getRole());
    }
}