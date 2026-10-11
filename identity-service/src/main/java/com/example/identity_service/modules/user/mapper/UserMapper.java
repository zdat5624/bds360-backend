package com.example.identity_service.modules.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.identity_service.common.mapper.MapperConfiguration;
import com.example.identity_service.modules.auth.dto.request.RegisterRequest;
import com.example.identity_service.modules.user.dto.request.CreateUserRequest;
import com.example.identity_service.modules.user.dto.request.UpdateProfileRequest;
import com.example.identity_service.modules.user.dto.request.UpdateUserRequest;
import com.example.identity_service.modules.user.dto.response.UserResponse;
import com.example.identity_service.modules.user.entity.User;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    UserResponse toUserResponse(User user);

    // 1. Chuyển DTO của Admin thành Entity
    User toUser(CreateUserRequest request);

    // 2. Chuyển DTO của Khách thành Entity
    User toUser(RegisterRequest request);

    // 2. Map dữ liệu từ Admin Update Request vào Entity hiện tại (Bỏ qua trường
    // null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    // 3. Map dữ liệu từ User Profile Request vào Entity hiện tại (Bỏ qua trường
    // null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileFromRequest(UpdateProfileRequest request, @MappingTarget User user);
}