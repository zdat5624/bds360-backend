package com.example.listing_service.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.listing_service.common.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ ElementType.METHOD, ElementType.TYPE }) // Có thể gắn lên Hàm hoặc Class
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
                // Luồng lỗi 400 (Validation)
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Lỗi dữ liệu đầu vào (Validation) hoặc Logic nghiệp vụ", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
                // Luồng lỗi 500 (Server)
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống máy chủ", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
})
public @interface ApiGlobalResponse {
}