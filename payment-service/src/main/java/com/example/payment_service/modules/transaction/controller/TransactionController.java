package com.example.payment_service.modules.transaction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment_service.common.annotation.ApiGlobalResponse;
import com.example.payment_service.common.dto.response.ApiResponse;
import com.example.payment_service.common.dto.response.PageResponse;
import com.example.payment_service.modules.transaction.dto.request.TransactionFilterRequest;
import com.example.payment_service.modules.transaction.dto.response.TransactionResponse;
import com.example.payment_service.modules.transaction.service.TransactionService;
import com.example.payment_service.security.annotation.CurrentUser;
import com.example.payment_service.security.annotation.IsAdmin;
import com.example.payment_service.security.annotation.RequireLogin;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@ApiGlobalResponse
@Tag(name = "transactions", description = "Quản lý giao dịch")
public class TransactionController {

    private final TransactionService transactionService;

    // ==========================================
    // QUẢN LÝ GIAO DỊCH (DÀNH CHO ADMIN)
    // ==========================================
    @GetMapping("/admin/transactions")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public ApiResponse<PageResponse<TransactionResponse>> getTransactions(
            @Valid @ModelAttribute TransactionFilterRequest filter) {

        return ApiResponse.success(transactionService.getTransactions(filter), "Lấy danh sách giao dịch thành công");
    }

    // ==========================================
    // LỊCH SỬ GIAO DỊCH (DÀNH CHO USER)
    // ==========================================
    @GetMapping("/transactions/my-transactions")
    @ResponseStatus(HttpStatus.OK)
    @RequireLogin
    public ApiResponse<PageResponse<TransactionResponse>> getMyTransactions(
            @CurrentUser User user,
            @Valid @ModelAttribute TransactionFilterRequest filter) {

        // Truyền thẳng User hoặc UserID xuống Service
        return ApiResponse.success(transactionService.getCurrentUserTransactions(user, filter),
                "Lấy lịch sử giao dịch cá nhân thành công");
    }

    // ==========================================
    // CHI TIẾT GIAO DỊCH
    // ==========================================
    @GetMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @RequireLogin
    public ApiResponse<TransactionResponse> getTransactionById(
            @PathVariable Long id,
            @CurrentUser User user) {

        return ApiResponse.success(transactionService.getTransactionById(id, user),
                "Lấy chi tiết giao dịch thành công");
    }
}