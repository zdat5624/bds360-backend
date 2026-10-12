package com.example.payment_service.modules.transaction.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.payment_service.modules.transaction.dto.request.TransactionFilterRequest;
import com.example.payment_service.modules.transaction.entity.Transaction;

import jakarta.persistence.criteria.Predicate;

public class TransactionSpecification {

    public static Specification<Transaction> filterTransactions(TransactionFilterRequest filter, Long targetUserId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Lọc theo User đang đăng nhập (So sánh thẳng với cột userId)
            if (targetUserId != null) {
                predicates.add(cb.equal(root.get("userId"), targetUserId));
            }

            // 2. Lọc theo Email (Không cần Join nữa, cực kỳ nhanh)
            if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("userEmail"), filter.getEmail().trim()));
            }

            // 3. Lọc theo ID giao dịch nội bộ
            if (filter.getTransactionId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getTransactionId()));
            }

            // 4. Lọc theo mã giao dịch VNPAY (txnId)
            if (filter.getTxnId() != null && !filter.getTxnId().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("txnId"), filter.getTxnId().trim()));
            }

            // 5. Lọc theo trạng thái (SUCCESS, PENDING, FAILED)
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            // 6. Lọc theo loại giao dịch (DEPOSIT: nạp tiền, PAYMENT: trừ tiền)
            if (filter.getType() != null) {
                predicates.add(cb.equal(root.get("type"), filter.getType()));
            }

            // 7. Lọc theo khoảng thời gian tạo giao dịch
            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}