// --- File: user/repository/VerificationSubmissionRepository.java ---
package com.example.identity_service.modules.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.identity_service.modules.user.constant.VerificationStatus;
import com.example.identity_service.modules.user.entity.VerificationSubmission;

public interface VerificationSubmissionRepository extends // 🌟 Đổi tên interface
                JpaRepository<VerificationSubmission, Long>, // 🌟 Đổi Entity type
                JpaSpecificationExecutor<VerificationSubmission> {

        boolean existsByUserIdAndStatus(Long userId, VerificationStatus status);

        Optional<VerificationSubmission> findTopByUserIdOrderByCreatedAtDesc(Long userId);

        Page<VerificationSubmission> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

        Optional<VerificationSubmission> findTopByUserIdAndStatusOrderByCreatedAtDesc(Long userId,
                        VerificationStatus status);

        long countByStatus(VerificationStatus status);
}