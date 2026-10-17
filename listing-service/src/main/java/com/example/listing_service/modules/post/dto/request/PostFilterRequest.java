
package com.example.listing_service.modules.post.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.example.listing_service.common.constant.ListingType;
import com.example.listing_service.common.dto.request.BaseFilterRequest;
import com.example.listing_service.modules.post.constant.CompassDirection;
import com.example.listing_service.modules.post.constant.Furnishing;
import com.example.listing_service.modules.post.constant.LegalStatus;
import com.example.listing_service.modules.post.constant.PostStatus;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostFilterRequest extends BaseFilterRequest {
    private Long minPrice;
    private Long maxPrice;
    private Double minArea;
    private Double maxArea;
    private Long categoryId;
    private ListingType type;
    private Long provinceCode;
    private Long districtCode;
    private Long wardCode;
    private Long vipId;
    private String search;

    private Boolean isHidden;

    // 🌟 THÊM TRƯỜNG NÀY (Mặc định tìm theo ID)
    private List<String> searchBy = new ArrayList<>(List.of("id"));

    private Boolean isDeleteByUser = false;
    private Boolean isApprovedOnly = false;

    private Integer bedrooms;
    private Integer bathrooms;
    private CompassDirection houseDirection;
    private CompassDirection balconyDirection;
    private LegalStatus legalStatus;
    private Furnishing furnishing;

    private List<PostStatus> statuses;
    private String userEmail;
    private Integer userId;

    @Pattern(regexp = "^(id|pushedAt|createdAt|price|area|view|title|status|expireDate)$", message = "Trường sắp xếp (sortBy) không hợp lệ đối với tin đăng.")
    private String sortBy = "pushedAt";
}