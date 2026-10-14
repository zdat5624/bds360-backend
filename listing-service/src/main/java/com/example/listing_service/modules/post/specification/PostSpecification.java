package com.example.listing_service.modules.post.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.listing_service.modules.post.constant.PostStatus;
import com.example.listing_service.modules.post.dto.request.PostFilterRequest;
import com.example.listing_service.modules.post.entity.ListingDetail;
import com.example.listing_service.modules.post.entity.Post;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class PostSpecification {

	public static Specification<Post> filterBy(PostFilterRequest filter) {
		return (root, query, cb) -> {
			Predicate predicate = cb.conjunction();

			// --- LỌC BẢNG CHÍNH (POST) ---
			if (filter.getMinPrice() != null)
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
			if (filter.getMaxPrice() != null)
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
			if (filter.getMinArea() != null)
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("area"), filter.getMinArea()));
			if (filter.getMaxArea() != null)
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("area"), filter.getMaxArea()));

			if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
				predicate = cb.and(predicate, root.get("status").in(filter.getStatuses()));
			}

			if (filter.getCategoryId() != null)
				predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), filter.getCategoryId()));
			if (filter.getType() != null)
				predicate = cb.and(predicate, cb.equal(root.get("type"), filter.getType()));
			if (filter.getProvinceCode() != null)
				predicate = cb.and(predicate, cb.equal(root.get("province").get("code"), filter.getProvinceCode()));
			if (filter.getDistrictCode() != null)
				predicate = cb.and(predicate, cb.equal(root.get("district").get("code"), filter.getDistrictCode()));
			if (filter.getWardCode() != null)
				predicate = cb.and(predicate, cb.equal(root.get("ward").get("code"), filter.getWardCode()));
			if (filter.getVipId() != null)
				predicate = cb.and(predicate, cb.equal(root.get("vip").get("id"), filter.getVipId()));
			if (Boolean.TRUE.equals(filter.getIsApprovedOnly())) {
				predicate = cb.and(predicate, root.get("status").in(PostStatus.APPROVED, PostStatus.REVIEW_LATER));
			}
			if (filter.getIsDeleteByUser() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("deletedByUser"), filter.getIsDeleteByUser()));
			}

			// 🌟 FIX 1: Lọc bằng userId (Thay vì root.get("user").get("id"))
			if (filter.getUserId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("userId"), filter.getUserId()));
			}

			if (filter.getIsHidden() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("isHidden"), filter.getIsHidden()));
			}

			// 🌟 FIX 2: Không Join bảng User nữa, tìm thẳng email trong trường createdBy
			if (filter.getUserEmail() != null && !filter.getUserEmail().isEmpty()) {
				predicate = cb.and(predicate, cb.equal(root.get("createdBy"), filter.getUserEmail()));
			}

			// 2. LOGIC TÌM KIẾM ĐỘNG DỰA TRÊN `searchBy`
			if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty() && filter.getSearchBy() != null
					&& !filter.getSearchBy().isEmpty()) {
				String keyword = filter.getSearch().trim();
				List<Predicate> searchPredicates = new ArrayList<>();

				for (String field : filter.getSearchBy()) {
					switch (field.toLowerCase()) {
						case "id":
							try {
								Long postId = Long.parseLong(keyword);
								searchPredicates.add(cb.equal(root.get("id"), postId));
							} catch (NumberFormatException e) {
								// Bỏ qua nếu người dùng truyền chuỗi
							}
							break;

						case "title":
							searchPredicates
									.add(cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"));
							break;

						case "description":
							searchPredicates
									.add(cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"));
							break;

						case "email":
							// 🌟 FIX 3: Tìm kiếm Like email bằng trường createdBy
							searchPredicates.add(cb.like(cb.lower(root.get("createdBy")),
									"%" + keyword.toLowerCase() + "%"));
							break;

						default:
							break;
					}
				}

				// Nối các điều kiện tìm kiếm bằng OR
				if (!searchPredicates.isEmpty()) {
					predicate = cb.and(predicate, cb.or(searchPredicates.toArray(new Predicate[0])));
				}
			}

			// ==========================================
			// LỌC BẢNG PHỤ (LISTING_DETAIL)
			// ==========================================
			Join<Post, ListingDetail> detailJoin = root.join("listingDetail", JoinType.LEFT);

			if (filter.getBedrooms() != null) {
				if (filter.getBedrooms() >= 5) {
					predicate = cb.and(predicate, cb.greaterThanOrEqualTo(detailJoin.get("bedrooms"), 5));
				} else {
					predicate = cb.and(predicate, cb.equal(detailJoin.get("bedrooms"), filter.getBedrooms()));
				}
			}

			if (filter.getBathrooms() != null) {
				if (filter.getBathrooms() >= 5) {
					predicate = cb.and(predicate, cb.greaterThanOrEqualTo(detailJoin.get("bathrooms"), 5));
				} else {
					predicate = cb.and(predicate, cb.equal(detailJoin.get("bathrooms"), filter.getBathrooms()));
				}
			}

			if (filter.getHouseDirection() != null) {
				predicate = cb.and(predicate, cb.equal(detailJoin.get("houseDirection"), filter.getHouseDirection()));
			}

			if (filter.getBalconyDirection() != null) {
				predicate = cb.and(predicate,
						cb.equal(detailJoin.get("balconyDirection"), filter.getBalconyDirection()));
			}

			if (filter.getLegalStatus() != null) {
				predicate = cb.and(predicate, cb.equal(detailJoin.get("legalStatus"), filter.getLegalStatus()));
			}

			if (filter.getFurnishing() != null) {
				predicate = cb.and(predicate, cb.equal(detailJoin.get("furnishing"), filter.getFurnishing()));
			}

			return predicate;
		};
	}
}