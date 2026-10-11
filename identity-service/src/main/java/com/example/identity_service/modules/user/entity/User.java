package com.example.identity_service.modules.user.entity;

import java.time.Instant;
import java.util.List;

import com.example.identity_service.common.constant.Role;
import com.example.identity_service.modules.auth.entity.PasswordResetToken;
import com.example.identity_service.modules.user.constant.Gender;
import com.example.identity_service.security.SecurityService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password không được để trống")
    @JsonIgnore
    private String password; // Trong DB có thể map với cột password_hash

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Long balance = 0L; // Giữ lại balance mặc định là 0

    private String avatar;

    @NotBlank(message = "Phone không được để trống")
    private String phone;

    private String address;

    @Column(nullable = false)
    private Boolean isVerified = false;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    // ĐÃ XÓA: posts, transactions, notifications (Chuyển sang Soft-link ở các
    // service khác)

    // GIỮ LẠI: Các quan hệ nội bộ của Identity Service
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<PasswordResetToken> passwordResetTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<VerificationSubmission> verificationSubmissions;

    @PrePersist
    public void handleBeforeCreate() {
        String currentUser = SecurityService.getCurrentUserLogin().orElse("anonymousUser");

        if ("anonymousUser".equals(currentUser) || currentUser.trim().isEmpty()) {
            this.createdBy = this.email;
        } else {
            this.createdBy = currentUser;
        }

        if (this.avatar == null || this.avatar.trim().isEmpty()) {
            this.avatar = "http://localhost:8080/uploads/avatar-default.webp"; // Có thể cấu hình lại URL qua
                                                                               // AppProperties
        }

        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }

        if (this.balance == null) {
            this.balance = 0L;
        }
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        // this.updatedBy = SecurityService.getCurrentUserLogin().orElse(this.email);
        this.updatedAt = Instant.now();
    }
}