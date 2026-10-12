package com.example.identity_service.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.identity_service.common.exception.ErrorCode;
import com.example.identity_service.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Truy vấn Database để lấy Entity User
        com.example.identity_service.modules.user.entity.User user = this.userService.handleGetUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException(ErrorCode.INVALID_CREDENTIALS.getMessage());
        }

        // Khởi tạo và trả về CustomUserDetails với dữ liệu từ Entity
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }

}