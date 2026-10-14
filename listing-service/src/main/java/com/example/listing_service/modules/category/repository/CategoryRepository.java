package com.example.listing_service.modules.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.listing_service.common.constant.ListingType;
import com.example.listing_service.modules.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Page<Category> findAll(Pageable pageable);

  @Query("""
          SELECT c FROM Category c
          WHERE (:type IS NULL OR c.type = :type)
            AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
      """)
  Page<Category> findByFilter(
      @Param("name") String name,
      @Param("type") ListingType type,
      Pageable pageable);
}
