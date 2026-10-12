package com.example.listing_service.modules.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.listing_service.modules.address.entity.Province;

import package com.example.listing_service.modules.address.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
}
