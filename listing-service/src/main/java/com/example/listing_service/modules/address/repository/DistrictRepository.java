package com.example.listing_service.modules.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.listing_service.modules.address.entity.District;

import package com.example.listing_service.modules.address.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByProvinceCode(Long provinceCode);
}
