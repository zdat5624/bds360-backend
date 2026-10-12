package com.example.payment_service.modules.transaction.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.payment_service.common.constant.Role;
import com.example.payment_service.common.dto.response.PageResponse;
import com.example.payment_service.common.exception.AppException;
import com.example.payment_service.common.exception.ErrorCode;
import com.example.payment_service.modules.transaction.dto.request.TransactionFilterRequest;
import com.example.payment_service.modules.transaction.dto.response.TransactionResponse;
import com.example.payment_service.modules.transaction.entity.Transaction;
import com.example.payment_service.modules.transaction.mapper.TransactionMapper;
import com.example.payment_service.modules.transaction.repository.TransactionRepository;
import com.example.payment_service.modules.transaction.specification.TransactionSpecification;
import com.example.payment_service.security.dto.UserContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

        private final TransactionRepository transactionRepository;
        private final TransactionMapper transactionMapper;

        public TransactionResponse getTransactionById(Long id, UserContext currentUser) {
                Transaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

                // Kiểm tra: Nếu không phải Admin VÀ không phải chủ sở hữu thì chặn
                boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
                boolean isOwner = transaction.getUserId().equals(currentUser.getId());

                if (!isAdmin && !isOwner) {
                        throw new AppException(ErrorCode.FORBIDDEN);
                }

                return transactionMapper.toTransactionResponse(transaction);
        }

        // ==========================================
        // Dành cho ADMIN
        // ==========================================
        public PageResponse<TransactionResponse> getTransactions(TransactionFilterRequest filter) {

                Sort sort = Sort.by(filter.getSortDirection(), filter.getSortBy());
                Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

                // Truyền null cho targetUserId để lấy toàn bộ giao dịch
                Page<Transaction> page = transactionRepository.findAll(
                                TransactionSpecification.filterTransactions(filter, null),
                                pageable);

                return PageResponse.of(page.map(transactionMapper::toTransactionResponse));
        }

        // ==========================================
        // Dành cho USER (Chỉ lấy giao dịch của chính họ)
        // ==========================================
        public PageResponse<TransactionResponse> getCurrentUserTransactions(Long userId,
                        TransactionFilterRequest filter) {

                Sort sort = Sort.by(filter.getSortDirection(), filter.getSortBy());
                Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

                Page<Transaction> page = transactionRepository.findAll(
                                TransactionSpecification.filterTransactions(filter, userId),
                                pageable);

                return PageResponse.of(page.map(transactionMapper::toTransactionResponse));
        }
}