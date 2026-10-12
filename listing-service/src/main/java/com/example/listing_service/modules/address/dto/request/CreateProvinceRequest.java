package com.example.listing_service.modules.address.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateProvinceRequest {

    private long code;
    private String name;
    private String codename;
    @JsonProperty("division_type")
    private String divisionType;
    @JsonProperty("phone_code")
    private int phoneCode;

    private List<CreateDistrictRequest> districts;

}