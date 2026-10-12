// --- File: user/controller/VerificationController.java ---
package com.example.identity_service.modules.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity_service.common.annotation.ApiGlobalResponse;
import com.example.identity_service.common.dto.request.BaseFilterRequest;
import com.example.identity_service.common.dto.response.ApiResponse;
import com.example.identity_service.common.dto.response.PageResponse;
import com.example.identity_service.modules.user.dto.request.ReviewVerificationRequest;
import com.example.identity_service.modules.user.dto.request.SubmitVerificationRequest;
import com.example.identity_service.modules.user.dto.request.VerificationFilterRequest;
import com.example.identity_service.modules.user.dto.response.VerificationResponse;
import com.example.identity_service.modules.user.service.VerificationService;
import com.example.identity_service.security.annotation.CurrentUser;
import com.example.identity_service.security.annotation.IsAdminOrModerator;
import com.example.identity_service.security.annotation.RequireLogin;
import com.example.identity_service.security.dto.UserContext;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@ApiGlobalResponse
@Tag(name = "verification", description = "Quản lý xác thực tài khoản (CCCD/CMND)")
public class VerificationController {

    private final VerificationService verificationService;

    // ==========================================
    // NHÓM API DÀNH CHO ADMIN / MODERATOR
    // ==========================================

    @GetMapping("/manage/verification-requests")
    @ResponseStatus(HttpStatus.OK)
    @IsAdminOrModerator
    public ApiResponse<PageResponse<VerificationResponse>> getVerificationRequests(
            @Valid @ModelAttribute VerificationFilterRequest filter) {
        return ApiResponse.success(
                verificationService.getVerificationRequests(filter),
                "Lấy danh sách yêu cầu xác thực thành công");
    }

    @PutMapping("/manage/verification-requests/{requestId}/review")
    @ResponseStatus(HttpStatus.OK)
    @IsAdminOrModerator
    public ApiResponse<Void> reviewVerification(
            @PathVariable Long requestId,
            @Valid @RequestBody ReviewVerificationRequest request) {
        verificationService.reviewRequest(requestId, request);
        return ApiResponse.success(null, "Đã xử lý hồ sơ xác thực.");
    }

    // 🌟 API đã được cập nhật để gọi logic "Smart Latest" của bạn
    @GetMapping("/manage/verification-requests/users/{userId}/latest")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<VerificationResponse> getLatestVerification(@PathVariable Long userId) {
        return ApiResponse.success(
                verificationService.getLatestVerificationInfo(userId), // 👈 Đã đổi tên hàm ở đây
                "Lấy hồ sơ xác thực mới nhất thành công");
    }

    @GetMapping("/users/verification/latest")
    @ResponseStatus(HttpStatus.OK)
    @RequireLogin
    public ApiResponse<VerificationResponse> getMyLatestVerification(@CurrentUser UserContext user) {
        // Tái sử dụng hàm của Service, nhưng ép cứng ID là của người đang đăng nhập
        return ApiResponse.success(
                verificationService.getLatestVerificationInfo(user.getId()),
                "Lấy hồ sơ xác thực mới nhất thành công");
    }

    // ==========================================
    // NHÓM API DÀNH CHO NGƯỜI DÙNG (USER)
    // ==========================================

    @PostMapping("/users/verification/submit")
    @ResponseStatus(HttpStatus.CREATED)
    @RequireLogin
    public ApiResponse<Void> submitVerification(
            @CurrentUser UserContext user,
            @Valid @RequestBody SubmitVerificationRequest request) {
        verificationService.submitRequest(user.getEmail(), request);
        return ApiResponse.success(null, "Đã gửi yêu cầu xác thực. Vui lòng chờ hệ thống kiểm duyệt.");
    }

    @GetMapping("/users/verification/history")
    @ResponseStatus(HttpStatus.OK)
    @RequireLogin
    public ApiResponse<PageResponse<VerificationResponse>> getMyVerificationHistory(
            @CurrentUser UserContext user,
            @Valid @ModelAttribute BaseFilterRequest filter) {

        return ApiResponse.success(
                verificationService.getMyVerificationHistory(user.getId(), filter),
                "Lấy lịch sử xác thực thành công");
    }
}