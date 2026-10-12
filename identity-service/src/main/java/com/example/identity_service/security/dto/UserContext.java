package com.example.identity_service.security.dto;

import com.example.identity_service.common.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserContext {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
}