// --- File: user/specification/VerificationSpecification.java ---
package com.example.identity_service.modules.user.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.identity_service.modules.user.dto.request.VerificationFilterRequest;
import com.example.identity_service.modules.user.entity.User;
import com.example.identity_service.modules.user.entity.VerificationSubmission;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class VerificationSpecification {

    public static Specification<VerificationSubmission> filter(VerificationFilterRequest filter) { // 🌟 Đổi kiểu trả về
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filter.getStatus() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
                Join<VerificationSubmission, User> userJoin = root.join("user"); // 🌟 Đổi kiểu Join
                String searchPattern = "%" + filter.getSearch().trim().toLowerCase() + "%";

                Predicate searchByName = cb.like(cb.lower(userJoin.get("name")), searchPattern);
                Predicate searchByEmail = cb.like(cb.lower(userJoin.get("email")), searchPattern);

                predicate = cb.and(predicate, cb.or(searchByName, searchByEmail));
            }

            return predicate;
        };
    }
}