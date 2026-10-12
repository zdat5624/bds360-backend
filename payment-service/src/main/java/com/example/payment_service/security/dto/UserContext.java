package com.example.payment_service.security.dto;

import com.example.payment_service.common.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
}