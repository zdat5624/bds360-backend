package com.example.listing_service.modules.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listing_service.modules.address.entity.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByProvinceCode(Long provinceCode);
}
