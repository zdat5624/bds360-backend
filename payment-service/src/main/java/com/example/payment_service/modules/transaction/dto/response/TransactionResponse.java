package com.example.payment_service.modules.transaction.dto.response;

import java.time.Instant;

import com.example.payment_service.modules.transaction.constant.TransactionStatus;
import com.example.payment_service.modules.transaction.constant.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {

    private Long id;

    private Long amount;

    private TransactionStatus status;

    private String paymentLink;

    private String txnId;

    private TransactionType type;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TransactionUserResponse user;

    @Getter
    @Setter
    public static class TransactionUserResponse {
        private Long id;
        private String name;
        private String email;
    }
}