// vn/bds360/backend/modules/post/dto/request/ForYouPostRequest.java
package com.example.listing_service.modules.post.dto.request;

import java.util.List;

import com.example.listing_service.common.constant.ListingType;
import com.example.listing_service.common.dto.request.BaseFilterRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForYouPostRequest extends BaseFilterRequest {
    // Cho phép Frontend lọc thêm: Gợi ý nhưng chỉ lấy tin BÁN (SALE) hoặc CHO THUÊ
    // (RENT)
    // Nếu Frontend không truyền (null), backend sẽ lấy cả 2 loại
    private ListingType type;

    private List<Long> excludeIds;
}