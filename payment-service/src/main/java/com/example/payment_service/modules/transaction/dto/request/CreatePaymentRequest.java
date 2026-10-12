package com.example.payment_service.modules.transaction.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {

    @Min(value = 10000, message = "Số tiền nạp tối thiểu là 10.000 VNĐ")
    private long amount;
}