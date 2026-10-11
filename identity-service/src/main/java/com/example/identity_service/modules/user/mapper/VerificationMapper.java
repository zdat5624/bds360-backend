// --- File: user/mapper/VerificationMapper.java ---
package com.example.identity_service.modules.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.identity_service.common.mapper.MapperConfiguration;
import com.example.identity_service.modules.user.dto.response.VerificationResponse;
import com.example.identity_service.modules.user.entity.VerificationSubmission;

@Mapper(config = MapperConfiguration.class)
public interface VerificationMapper {

    // Map các trường lồng nhau (nested fields) từ entity User sang Response
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userAvatar", source = "user.avatar")
    VerificationResponse toResponse(VerificationSubmission verificationSubmission); // 🌟 Đổi kiểu tham số

}