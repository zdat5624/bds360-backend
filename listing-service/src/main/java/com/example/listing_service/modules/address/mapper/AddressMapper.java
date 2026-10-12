package com.example.listing_service.modules.address.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.listing_service.modules.address.dto.response.DistrictResponse;
import com.example.listing_service.modules.address.dto.response.ProvinceResponse;
import com.example.listing_service.modules.address.dto.response.WardResponse;
import com.example.listing_service.modules.address.entity.District;
import com.example.listing_service.modules.address.entity.Province;
import com.example.listing_service.modules.address.entity.Ward;

import vn.bds360.backend.common.mapper.MapperConfiguration;import package com.example.listing_service.modules.address.dto.response.DistrictResponse;import package com.example.listing_service.modules.address.dto.response.ProvinceResponse;import package com.example.listing_service.modules.address.dto.response.WardResponse;import package com.example.listing_service.modules.address.entity.District;import package com.example.listing_service.modules.address.entity.Province;import package com.example.listing_service.modules.address.entity.Ward;

@Mapper(config = MapperConfiguration.class)
public interface AddressMapper {

    // 1. Province Mapping
    ProvinceResponse toProvinceResponse(Province province);

    List<ProvinceResponse> toProvinceResponseList(List<Province> provinces);

    // 2. District Mapping
    DistrictResponse toDistrictResponse(District district);

    List<DistrictResponse> toDistrictResponseList(List<District> districts);

    // 3. Ward Mapping
    WardResponse toWardResponse(Ward ward);

    List<WardResponse> toWardResponseList(List<Ward> wards);
}