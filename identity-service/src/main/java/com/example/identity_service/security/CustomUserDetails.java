package com.example.identity_service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

    private final Long id;
    private final String email;
    private final String name;
    private final String phone;

    public CustomUserDetails(Long id, String email, String name, String phone, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities); // Gọi hàm khởi tạo của Spring User
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
