// --- File: user/service/VerificationService.java ---
package com.example.identity_service.modules.user.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.identity_service.common.dto.request.BaseFilterRequest;
import com.example.identity_service.common.dto.response.PageResponse;
import com.example.identity_service.common.exception.AppException;
import com.example.identity_service.common.exception.ErrorCode;
import com.example.identity_service.modules.user.constant.VerificationStatus;
import com.example.identity_service.modules.user.dto.request.ReviewVerificationRequest;
import com.example.identity_service.modules.user.dto.request.SubmitVerificationRequest;
import com.example.identity_service.modules.user.dto.request.VerificationFilterRequest;
import com.example.identity_service.modules.user.dto.response.VerificationResponse;
import com.example.identity_service.modules.user.entity.User;
import com.example.identity_service.modules.user.entity.VerificationSubmission;
import com.example.identity_service.modules.user.mapper.VerificationMapper;
import com.example.identity_service.modules.user.repository.UserRepository;
import com.example.identity_service.modules.user.repository.VerificationSubmissionRepository;
import com.example.identity_service.modules.user.specification.VerificationSpecification;
import com.example.identity_service.security.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationSubmissionRepository verificationRepo; // 🌟 Đổi kiểu biến
    private final UserRepository userRepository;

    private final VerificationMapper verificationMapper;

    // ==========================================
    // 1. LẤY DANH SÁCH (DÀNH CHO ADMIN/MOD)
    // ==========================================
    public PageResponse<VerificationResponse> getVerificationRequests(VerificationFilterRequest filter) {
        Sort sort = Sort.by(filter.getSortDirection(), filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Page<VerificationSubmission> pageData = verificationRepo.findAll(
                VerificationSpecification.filter(filter),
                pageable);

        Page<VerificationResponse> dtoPage = pageData.map(verificationMapper::toResponse);

        return PageResponse.of(dtoPage);
    }

    // ==========================================
    // 2. USER NỘP ĐƠN XÁC THỰC
    // ==========================================
    @Transactional
    public void submitRequest(String email, SubmitVerificationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new AppException(ErrorCode.USER_ALREADY_VERIFIED);
        }

        if (verificationRepo.existsByUserIdAndStatus(user.getId(), VerificationStatus.PENDING)) {
            throw new AppException(ErrorCode.VERIFICATION_REQUEST_PENDING);
        }

        VerificationSubmission newSubmission = VerificationSubmission.builder() // 🌟 Đổi lớp Builder
                .user(user)
                .idCardFront(request.getIdCardFront())
                .idCardBack(request.getIdCardBack())
                .build();

        verificationRepo.save(newSubmission);
    }

    // ==========================================
    // 3. ADMIN/MOD DUYỆT ĐƠN
    // ==========================================
    @Transactional
    public void reviewRequest(Long submissionId, ReviewVerificationRequest request) { // 🌟 Đổi tham số cho rõ nghĩa
        VerificationSubmission submission = verificationRepo.findById(submissionId) // 🌟 Đổi kiểu biến
                .orElseThrow(() -> new AppException(ErrorCode.VERIFICATION_REQUEST_NOT_FOUND));

        if (submission.getStatus() != VerificationStatus.PENDING) {
            throw new AppException(ErrorCode.VERIFICATION_REQUEST_ALREADY_PROCESSED);
        }

        if (request.getStatus() == VerificationStatus.REJECTED &&
                (request.getNote() == null || request.getNote().trim().isEmpty())) {
            throw new AppException(ErrorCode.REJECT_NOTE_REQUIRED);
        }

        submission.setStatus(request.getStatus());
        submission.setReviewNote(request.getNote());
        submission.setReviewedAt(Instant.now());
        submission.setReviewedBy(SecurityService.getCurrentUserLogin().orElse("System"));

        if (request.getStatus() == VerificationStatus.APPROVED) {
            User user = submission.getUser();
            user.setIsVerified(true);
            userRepository.save(user);
        }

        verificationRepo.save(submission);
    }

    // ==========================================
    // 5. LẤY HỒ SƠ MỚI NHẤT (DÀNH CHO ADMIN/MOD)
    // ==========================================
    public VerificationResponse getLatestVerificationInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<VerificationSubmission> submission;

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            // 🌟 Nếu đã xác thực: Phải lấy đúng cái bản đã được APPROVED
            submission = verificationRepo.findTopByUserIdAndStatusOrderByCreatedAtDesc(userId,
                    VerificationStatus.APPROVED);
        } else {
            // Nếu chưa xác thực: Lấy cái mới nhất (có thể là PENDING hoặc REJECTED cũ)
            submission = verificationRepo.findTopByUserIdOrderByCreatedAtDesc(userId);
        }

        return submission.map(verificationMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.VERIFICATION_REQUEST_NOT_FOUND));
    }

    // TỐI ƯU: Xóa hàm mapToResponse thủ công cũ ở cuối file và dùng Mapper đồng bộ
    // Ví dụ trong hàm getMyVerificationHistory:
    public PageResponse<VerificationResponse> getMyVerificationHistory(Long userId, BaseFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<VerificationSubmission> pageData = verificationRepo.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        // Sử dụng mapper thống nhất
        return PageResponse.of(pageData.map(verificationMapper::toResponse));
    }
}