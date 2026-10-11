package com.example.identity_service.modules.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identity_service.common.constant.Role;
import com.example.identity_service.common.dto.response.PageResponse;
import com.example.identity_service.common.exception.AppException;
import com.example.identity_service.common.exception.ErrorCode;
import com.example.identity_service.modules.user.dto.request.CreateUserRequest;
import com.example.identity_service.modules.user.dto.request.UpdateProfileRequest;
import com.example.identity_service.modules.user.dto.request.UpdateUserRequest;
import com.example.identity_service.modules.user.dto.request.UserFilterRequest;
import com.example.identity_service.modules.user.dto.response.UserResponse;
import com.example.identity_service.modules.user.entity.User;
import com.example.identity_service.modules.user.mapper.UserMapper;
import com.example.identity_service.modules.user.repository.UserRepository;
import com.example.identity_service.modules.user.specification.UserSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse handleCreateUser(CreateUserRequest request) {
        if (isEmailExist(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public User saveInternalUser(User user) {
        return userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        User user = fetchUserById(id);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.getRole().equals(Role.ADMIN)) {
            throw new AppException(ErrorCode.CANNOT_DELETE_ADMIN);
        }
        userRepository.delete(user);
    }

    // Hàm nội bộ tìm User theo ID (ném lỗi nếu không thấy)
    public User fetchUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    // Hàm nội bộ tìm User theo Email (trả null nếu không thấy)
    public User handleGetUserByUserName(String username) {
        return userRepository.findByEmail(username).orElse(null);
    }

    // Đã đổi kiểu trả về thành UserResponse
    public UserResponse fetchUserByIdWithPermission(long targetUserId, String email) {
        User currentUser = handleGetUserByUserName(email);

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isModerator = currentUser.getRole().equals(Role.MODERATOR); // Thêm quyền Mod
        boolean isOwner = currentUser.getId() == targetUserId;

        if (!isAdmin && !isModerator && !isOwner) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        User targetUser = fetchUserById(targetUserId);
        return userMapper.toUserResponse(targetUser);
    }

    public UserResponse handleUpdateUser(UpdateUserRequest request) {
        User currentUser = fetchUserById(request.getId());

        if (currentUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userMapper.updateUserFromRequest(request, currentUser);

        currentUser = userRepository.save(currentUser);
        return userMapper.toUserResponse(currentUser);
    }

    public UserResponse handleUpdateProfile(UpdateProfileRequest request, String currentUsername) {
        User currentUser = handleGetUserByUserName(currentUsername);

        if (currentUser.getId() != request.getId()) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        User targetUser = fetchUserById(request.getId());
        if (targetUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        userMapper.updateProfileFromRequest(request, targetUser);

        targetUser = userRepository.save(targetUser);
        return userMapper.toUserResponse(targetUser);
    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public PageResponse<UserResponse> getUsers(UserFilterRequest filter) {

        Sort sort = Sort.by(filter.getSortDirection(), filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Page<User> userPage = userRepository.findAll(
                UserSpecification.filterUsers(filter),
                pageable);

        return PageResponse.of(userPage.map(userMapper::toUserResponse));
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = handleGetUserByUserName(email);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void forceUpdatePassword(String email, String encodedNewPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}