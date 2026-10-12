package com.example.listing_service.modules.address.dto.response;

import lombok.Data;

@Data
public class WardResponse {
    private long code;
    private String name;
    private String divisionType;
}