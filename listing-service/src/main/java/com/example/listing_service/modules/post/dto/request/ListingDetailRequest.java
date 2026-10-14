package com.example.listing_service.modules.post.dto.request;

import com.example.listing_service.modules.post.constant.CompassDirection;
import com.example.listing_service.modules.post.constant.Furnishing;
import com.example.listing_service.modules.post.constant.LegalStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ListingDetailRequest {

    @Min(value = 1, message = "Số phòng ngủ phải từ 1 trở lên")
    @Max(value = 100, message = "Số phòng ngủ không hợp lệ")
    private Integer bedrooms;

    @Min(value = 1, message = "Số phòng tắm, vệ sinh phải từ 1 trở lên")
    @Max(value = 100, message = "Số phòng tắm không hợp lệ")
    private Integer bathrooms;

    private CompassDirection houseDirection;

    private CompassDirection balconyDirection;

    private LegalStatus legalStatus;

    private Furnishing furnishing;
}