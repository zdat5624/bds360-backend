// --- File: AdminPostController.java ---
package com.example.listing_service.modules.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.listing_service.common.annotation.ApiGlobalResponse;
import com.example.listing_service.common.dto.response.ApiResponse;
import com.example.listing_service.common.dto.response.PageResponse;
import com.example.listing_service.modules.post.dto.request.PostFilterRequest;
import com.example.listing_service.modules.post.dto.request.UpdatePostStatusRequest;
import com.example.listing_service.modules.post.dto.response.PostResponse;
import com.example.listing_service.modules.post.service.PostService;
import com.example.listing_service.security.annotation.CurrentUser;
import com.example.listing_service.security.annotation.IsAdminOrModerator;
import com.example.listing_service.security.dto.UserContext;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// 🌟 Đổi endpoint từ /admin/posts thành /manage/posts
@RequestMapping("/api/v1/manage/posts")
@RequiredArgsConstructor
@Validated
@IsAdminOrModerator
@ApiGlobalResponse
@RestController
@Tag(name = "manage-posts", description = "Back-office: Quản lý bài đăng")
public class ManagePostController { // 🌟 Đổi tên class

    private final PostService postService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PageResponse<PostResponse>> getAdminPosts(@Valid PostFilterRequest filter) {
        return ApiResponse.success(postService.getManagePosts(filter));
    }

    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostResponse> updatePostStatus(@Valid @RequestBody UpdatePostStatusRequest dto) {
        return ApiResponse.success(postService.updatePostStatus(
                dto.getPostId(),
                dto.getStatus(),
                dto.getMessage(),
                dto.isSendNotification()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deletePostAdmin(@CurrentUser UserContext adminOrMod, @PathVariable Long id) {
        postService.deletePost(adminOrMod, id, true);
        return ApiResponse.success(null, "Hệ thống đã gỡ bỏ tin đăng thành công"); // 🌟 Sửa câu thông báo
    }
}