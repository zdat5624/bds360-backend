package com.example.listing_service.modules.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.listing_service.modules.address.entity.Ward;

import package com.example.listing_service.modules.address.entity.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByDistrictCode(Long districtCode);
}
