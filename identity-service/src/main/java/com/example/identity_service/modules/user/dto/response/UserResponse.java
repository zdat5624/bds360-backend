package com.example.identity_service.modules.user.dto.response;

import java.time.Instant;

import com.example.identity_service.common.constant.Role;
import com.example.identity_service.modules.user.constant.Gender;

import lombok.Getter;
import lombok.Setter;;

@Getter
@Setter
public class UserResponse {
    private long id;
    private String name;
    private String email;
    private Role role;
    private Gender gender;
    private long balance;
    private String phone;
    private String address;
    private String avatar;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private Boolean isVerified;

}