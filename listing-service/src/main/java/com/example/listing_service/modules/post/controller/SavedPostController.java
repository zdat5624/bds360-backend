package com.example.listing_service.modules.post.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.listing_service.common.annotation.ApiGlobalResponse;
import com.example.listing_service.common.dto.request.BaseFilterRequest;
import com.example.listing_service.common.dto.response.ApiResponse;
import com.example.listing_service.common.dto.response.PageResponse;
import com.example.listing_service.modules.post.dto.response.SavedPostResponse;
import com.example.listing_service.modules.post.service.SavedPostService;
import com.example.listing_service.security.annotation.CurrentUser;
import com.example.listing_service.security.dto.UserContext;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@ApiGlobalResponse
@Tag(name = "saved-post", description = "Quản lý lưu tin đăng")
public class SavedPostController {

    private final SavedPostService savedPostService;

    @PostMapping("/{id}/save")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> savePost(@CurrentUser UserContext currentUser, @PathVariable("id") Long postId) {
        savedPostService.savePost(currentUser, postId);
        return ApiResponse.success(null, "Lưu tin thành công");
    }

    @DeleteMapping("/{id}/save")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> unsavePost(@CurrentUser UserContext currentUser, @PathVariable("id") Long postId) {
        savedPostService.unsavePost(currentUser, postId);
        return ApiResponse.success(null, "Đã bỏ lưu tin");
    }

    @GetMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PageResponse<SavedPostResponse>> getMySavedPosts(
            @CurrentUser UserContext currentUser,
            @Valid @ModelAttribute BaseFilterRequest filterRequest) {

        PageResponse<SavedPostResponse> savedPosts = savedPostService.getSavedPosts(currentUser, filterRequest);

        return ApiResponse.success(savedPosts, "Lấy danh sách tin đã lưu thành công");
    }

    @PostMapping("/saved/check")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Map<Long, Boolean>> checkSavedStatus(
            @CurrentUser UserContext currentUser,
            @RequestBody List<Long> postIds) {

        Map<Long, Boolean> result = savedPostService.checkSavedStatus(currentUser, postIds);

        return ApiResponse.success(result, "Kiểm tra trạng thái lưu thành công");
    }
}