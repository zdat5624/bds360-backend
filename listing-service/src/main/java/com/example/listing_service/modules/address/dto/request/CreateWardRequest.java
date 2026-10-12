package com.example.listing_service.modules.address.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateWardRequest {
    private long code;
    private String name;
    private String codename;
    @JsonProperty("division_type")
    private String divisionType;
    @JsonProperty("short_codename")
    private String shortCodename;

}