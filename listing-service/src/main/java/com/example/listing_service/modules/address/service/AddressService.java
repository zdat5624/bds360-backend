package com.example.listing_service.modules.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.listing_service.modules.address.dto.response.CoordinateResponse;
import com.example.listing_service.modules.address.dto.response.DistrictResponse;
import com.example.listing_service.modules.address.dto.response.ProvinceResponse;
import com.example.listing_service.modules.address.dto.response.WardResponse;
import com.example.listing_service.modules.address.mapper.AddressMapper;
import com.example.listing_service.modules.address.repository.DistrictRepository;
import com.example.listing_service.modules.address.repository.ProvinceRepository;
import com.example.listing_service.modules.address.repository.WardRepository;

import lombok.RequiredArgsConstructor;
import vn.bds360.backend.common.exception.AppException;
import vn.bds360.backend.common.exception.ErrorCode;import package com.example.listing_service.modules.address.dto.response.CoordinateResponse;import package com.example.listing_service.modules.address.dto.response.DistrictResponse;import package com.example.listing_service.modules.address.dto.response.ProvinceResponse;import package com.example.listing_service.modules.address.dto.response.WardResponse;import package com.example.listing_service.modules.address.mapper.AddressMapper;import package com.example.listing_service.modules.address.repository.DistrictRepository;import package com.example.listing_service.modules.address.repository.ProvinceRepository;import package com.example.listing_service.modules.address.repository.WardRepository;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final MapboxGeocodeService mapboxGeocodeService;
    private final AddressMapper addressMapper; // <-- Inject Mapper vào đây

    public List<ProvinceResponse> getAllProvinces() {
        return addressMapper.toProvinceResponseList(provinceRepository.findAll());
    }

    public List<DistrictResponse> getDistrictsByProvince(Long provinceCode) {
        return addressMapper.toDistrictResponseList(districtRepository.findByProvinceCode(provinceCode));
    }

    public List<WardResponse> getWardsByDistrict(Long districtCode) {
        return addressMapper.toWardResponseList(wardRepository.findByDistrictCode(districtCode));
    }

    public CoordinateResponse getCoordinates(String address) {
        return mapboxGeocodeService.getLatLngFromAddress(address)
                // Lưu ý: Mapbox trả về [Longitude, Latitude]
                .map(coords -> new CoordinateResponse(coords[1], coords[0]))
                .orElseThrow(() -> new AppException(ErrorCode.GEOCODE_FAILED));
    }
}