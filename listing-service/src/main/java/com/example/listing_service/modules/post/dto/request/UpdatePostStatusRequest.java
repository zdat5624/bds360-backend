package com.example.listing_service.modules.post.dto.request;

import com.example.listing_service.modules.post.constant.PostStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePostStatusRequest {

    @NotNull(message = "ID tin đăng không được để trống")
    private long postId;

    @NotNull(message = "Trạng thái mới không được để trống")
    private PostStatus status;

    private String message;

    private boolean sendNotification = true;

}