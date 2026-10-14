// vn/bds360/backend/modules/post/dto/request/RelatedPostRequest.java
package com.example.listing_service.modules.post.dto.request;

import java.util.List;

import com.example.listing_service.common.dto.request.BaseFilterRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatedPostRequest extends BaseFilterRequest {

    // Chỉ nhận duy nhất danh sách ID cần loại trừ từ Frontend
    private List<Long> excludeIds;

}