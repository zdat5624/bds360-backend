package com.example.identity_service.modules.user.dto.request;

import java.time.Instant;

import com.example.identity_service.common.constant.Role;
import com.example.identity_service.common.dto.request.BaseFilterRequest;
import com.example.identity_service.modules.user.constant.Gender;

import lombok.Getter;
import lombok.Setter;;

@Getter
@Setter
public class UserFilterRequest extends BaseFilterRequest {

    private String name;
    private String email;
    private Role role;
    private Gender gender;
    private String phone;
    private Long minBalance;
    private Long maxBalance;
    private String address;
    private Instant createdFrom;
    private Instant createdTo;
}