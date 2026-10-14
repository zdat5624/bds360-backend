package com.example.listing_service.modules.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listing_service.modules.post.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
