package com.example.listing_service.modules.post.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.listing_service.common.dto.request.BaseFilterRequest;
import com.example.listing_service.common.dto.response.PageResponse;
import com.example.listing_service.common.exception.AppException;
import com.example.listing_service.common.exception.ErrorCode;
import com.example.listing_service.modules.post.dto.response.SavedPostResponse;
import com.example.listing_service.modules.post.entity.Post;
import com.example.listing_service.modules.post.entity.SavedPost;
import com.example.listing_service.modules.post.mapper.PostMapper;
import com.example.listing_service.modules.post.repository.PostRepository;
import com.example.listing_service.modules.post.repository.SavedPostRepository;
import com.example.listing_service.security.dto.UserContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedPostService {

    private final SavedPostRepository savedPostRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    // 1. LƯU TIN
    @Transactional
    public void savePost(UserContext user, Long postId) {
        if (savedPostRepository.existsByUserIdAndPostId(user.getId(), postId)) {
            throw new AppException(ErrorCode.POST_ALREADY_SAVED);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        SavedPost savedPost = SavedPost.builder()
                .userId(user.getId())
                .post(post)
                .build(); // savedAt tự động set qua @PrePersist

        savedPostRepository.save(savedPost);
    }

    // 2. BỎ LƯU TIN
    @Transactional
    public void unsavePost(UserContext user, Long postId) {
        SavedPost savedPost = savedPostRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_SAVED_NOT_FOUND));

        savedPostRepository.delete(savedPost);
    }

    @Transactional(readOnly = true)
    public PageResponse<SavedPostResponse> getSavedPosts(UserContext user, BaseFilterRequest filter) {
        String entitySortField;
        Sort.Direction direction = filter.getSortDirection();

        switch (filter.getSortBy().toLowerCase()) {
            case "price":
                entitySortField = "post.price";
                break;
            case "area":
                entitySortField = "post.area";
                break;
            case "latest": // Tin có ngày ĐĂNG mới nhất
                entitySortField = "post.createdAt";
                direction = Sort.Direction.DESC;
                break;
            case "oldest": // Tin được LƯU TRƯỚC NHẤT (Cũ nhất trong danh sách lưu)
                entitySortField = "savedAt";
                direction = Sort.Direction.ASC;
                break;
            case "newest": // Tin vừa MỚI LƯU xong
            default:
                entitySortField = "savedAt";
                direction = Sort.Direction.DESC;
                break;
        }

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(direction, entitySortField));

        // 1. Lấy dữ liệu từ Repo
        Page<SavedPost> savedPostPage = savedPostRepository.findByUserId(user.getId(), pageable);

        // 2. Map sang DTO SavedPostResponse
        Page<SavedPostResponse> dtoPage = savedPostPage.map(postMapper::toSavedPostResponse);

        // 3. Bọc vào PageResponse custom của bạn
        return PageResponse.of(dtoPage);

    }

    @Transactional(readOnly = true)
    public Map<Long, Boolean> checkSavedStatus(UserContext user, List<Long> postIds) {
        // 1. Nếu chưa login hoặc danh sách trống, mặc định tất cả là false
        if (user == null || postIds == null || postIds.isEmpty()) {
            return postIds != null
                    ? postIds.stream().distinct().collect(Collectors.toMap(id -> id, id -> false))
                    : Collections.emptyMap();
        }

        // 2. Lấy danh sách ID thực sự đã lưu từ Repository
        Set<Long> savedIds = savedPostRepository.findSavedPostIdsByUserIdAndPostIdIn(user.getId(), postIds);

        // 3. Map ngược lại: ID nào có trong savedIds thì true, không thì false
        return postIds.stream()
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        savedIds::contains));
    }
}