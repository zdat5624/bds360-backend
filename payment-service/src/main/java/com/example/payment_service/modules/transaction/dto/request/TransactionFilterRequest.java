package com.example.payment_service.modules.transaction.dto.request;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.payment_service.common.dto.request.BaseFilterRequest;
import com.example.payment_service.modules.transaction.constant.TransactionStatus;
import com.example.payment_service.modules.transaction.constant.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionFilterRequest extends BaseFilterRequest {

    // Ghi đè cột sắp xếp mặc định: Hiển thị giao dịch mới nhất lên đầu
    public TransactionFilterRequest() {
        super();
        this.setSortBy("createdAt");
    }

    private String email;
    private Long transactionId;
    private String txnId;
    private TransactionStatus status;
    private TransactionType type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant endDate;
}