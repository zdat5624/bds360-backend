package com.example.listing_service.modules.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listing_service.modules.address.entity.Province;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
}
