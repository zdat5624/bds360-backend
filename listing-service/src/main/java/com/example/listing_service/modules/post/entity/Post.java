package com.example.listing_service.modules.post.entity;

import java.time.Instant;
import java.util.List;

import com.example.listing_service.common.constant.ListingType;
import com.example.listing_service.modules.address.entity.District;
import com.example.listing_service.modules.address.entity.Province;
import com.example.listing_service.modules.address.entity.Ward;
import com.example.listing_service.modules.category.entity.Category;
import com.example.listing_service.modules.post.constant.PostStatus;
import com.example.listing_service.modules.vip.entity.Vip;
// Không còn import User entity nữa!
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được quá 255 ký tự")
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Loại tin đăng không được để trống")
    private ListingType type;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private Long price;

    @NotNull(message = "Diện tích không được để trống")
    @DecimalMin(value = "0.1", message = "Diện tích phải lớn hơn 0")
    private Double area;

    @Min(value = 0, message = "Lượt xem phải không âm")
    private Long view = 0L;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private Instant expireDate;
    private Boolean deletedByUser = false;

    @Column(name = "is_hidden")
    private Boolean isHidden = false;

    // ========================================================
    // 🌟 DENORMALIZATION: THÔNG TIN TÁC GIẢ (SOFT-LINK)
    // ========================================================
    @Column(name = "user_id", nullable = false)
    private Long userId; // Liên kết mềm sang bảng users của Identity Service

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_phone")
    private String authorPhone;

    @Column(name = "author_avatar")
    private String authorAvatar;

    // ========================================================
    // DỮ LIỆU ĐỊA LÝ (Giữ khóa ngoại nội bộ trong Listing Service)
    // ========================================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code")
    @NotNull(message = "Tỉnh/Thành phố không được để trống")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code")
    @NotNull(message = "Quận/Huyện không được để trống")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_code")
    private Ward ward;

    @Size(max = 255, message = "Địa chỉ chi tiết không được quá 255 ký tự")
    @NotNull
    private String streetAddress;

    private Double latitude;
    private Double longitude;

    // ========================================================
    // QUAN HỆ NỘI BỘ
    // ========================================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull(message = "Danh mục không được để trống")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vip_id")
    private Vip vip;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ListingDetail listingDetail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostViewHistory> postViewHistories;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SavedPost> savedPosts;

    // ========================================================
    // METADATA
    // ========================================================
    private Instant createdAt;
    private Instant updatedAt;
    private Instant pushedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.pushedAt == null) {
            this.pushedAt = this.createdAt;
        }
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}