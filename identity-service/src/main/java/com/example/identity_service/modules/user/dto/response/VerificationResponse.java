// --- File: user/dto/response/VerificationResponse.java ---
package com.example.identity_service.modules.user.dto.response;

import java.time.Instant;

import com.example.identity_service.modules.user.constant.VerificationStatus;

import lombok.Data;

@Data
public class VerificationResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String idCardFront;
    private String idCardBack;
    private VerificationStatus status;
    private String reviewNote;
    private Instant createdAt;
    private Instant reviewedAt;
    private String reviewedBy;
    private String userAvatar;

}