package com.example.identity_service.modules.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity_service.common.annotation.ApiGlobalResponse;
import com.example.identity_service.common.dto.response.ApiResponse;
import com.example.identity_service.modules.auth.dto.request.ChangePasswordRequest;
import com.example.identity_service.modules.auth.dto.request.ForgotPasswordRequest;
import com.example.identity_service.modules.auth.dto.request.GoogleLoginRequest;
import com.example.identity_service.modules.auth.dto.request.LoginRequest;
import com.example.identity_service.modules.auth.dto.request.RegisterRequest;
import com.example.identity_service.modules.auth.dto.request.ResetPasswordRequest;
import com.example.identity_service.modules.auth.dto.response.AuthResponse;
import com.example.identity_service.modules.auth.service.AuthService;
import com.example.identity_service.modules.auth.service.ForgotPasswordService;
import com.example.identity_service.modules.user.dto.response.UserResponse;
import com.example.identity_service.modules.user.service.UserService;
import com.example.identity_service.security.annotation.CurrentUser;
import com.example.identity_service.security.annotation.RequireLogin;
import com.example.identity_service.security.dto.UserContext;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@ApiGlobalResponse
@Tag(name = "auth", description = "Xác thực, đăng ký và khôi phục mật khẩu")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;
    private final UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), "Đăng nhập thành công");
    }

    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    @RequireLogin
    public ApiResponse<UserResponse> getAccount(@CurrentUser UserContext user) {
        System.out.println("User email from @CurrentUser: " + user.getEmail());
        return ApiResponse.success(authService.getAccount(user.getEmail()), "Lấy thông tin tài khoản thành công");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request), "Đăng ký tài khoản thành công");
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> requestPasswordReset(@Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordService.requestPasswordReset(request.getEmail());
        return ApiResponse.success(null, "Mã xác nhận đã được gửi đến email của bạn.");
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ApiResponse.success(null, "Đặt lại mật khẩu thành công.");
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @RequireLogin
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
            @CurrentUser UserContext user) {
        System.out.println(">>> User Context: " + user.toString());
        userService.changePassword(user.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        return ApiResponse.success(null, "Đổi mật khẩu thành công.");
    }

    @PostMapping("/google")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        return ApiResponse.success(authService.googleLogin(request), "Đăng nhập Google thành công");
    }
}