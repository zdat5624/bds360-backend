// --- File: user/dto/request/ReviewVerificationRequest.java ---
package com.example.identity_service.modules.user.dto.request;

import com.example.identity_service.modules.user.constant.VerificationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewVerificationRequest {
    @NotNull(message = "Trạng thái duyệt không được để trống")
    private VerificationStatus status; // Truyền VERIFIED hoặc REJECTED

    private String note; // Lý do từ chối (bắt buộc nếu status là REJECTED)
}