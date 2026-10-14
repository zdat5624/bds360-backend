package com.example.listing_service.modules.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listing_service.modules.address.entity.Ward;

public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByDistrictCode(Long districtCode);
}
