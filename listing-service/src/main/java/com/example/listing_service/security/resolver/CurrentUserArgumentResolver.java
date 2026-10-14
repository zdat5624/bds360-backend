package com.example.listing_service.security.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.listing_service.common.constant.Role;
import com.example.listing_service.common.exception.AppException;
import com.example.listing_service.common.exception.ErrorCode;
import com.example.listing_service.security.annotation.CurrentUser;
import com.example.listing_service.security.annotation.RequireLogin;
import com.example.listing_service.security.dto.UserContext;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Chỉ kích hoạt khi tham số có gắn @CurrentUser và kiểu dữ liệu là UserContext
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = null;

        // Trích xuất dữ liệu từ JWT Token (Hoàn toàn Stateless)
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {

            Long id = null;
            if (jwt.hasClaim("userId")) {
                id = ((Number) jwt.getClaim("userId")).longValue();
            }

            String email = jwt.getClaimAsString("userEmail");
            if (email == null)
                email = jwt.getSubject();

            String name = jwt.getClaimAsString("userName");
            String phone = jwt.getClaimAsString("userPhone");

            // 🌟 XỬ LÝ ÉP KIỂU ROLE (String -> Enum)
            Role userRole = Role.USER; // Gán mặc định là USER cho an toàn
            if (jwt.hasClaim("roles")) {
                java.util.List<String> roles = jwt.getClaimAsStringList("roles");
                if (roles != null && !roles.isEmpty()) {
                    String roleString = roles.get(0); // Lấy role đầu tiên (ví dụ: "ADMIN")

                    // Xóa tiền tố "ROLE_" nếu có (do Spring Security thỉnh thoảng tự thêm vào)
                    if (roleString.startsWith("ROLE_")) {
                        roleString = roleString.substring(5);
                    }

                    try {
                        userRole = Role.valueOf(roleString); // Biến String thành Enum
                    } catch (IllegalArgumentException e) {
                        System.out.println("Lỗi ép kiểu Role: Không khớp với Enum " + roleString);
                    }
                }
            }

            // Trả về UserContext với kiểu Enum chuẩn xác
            userContext = new UserContext(id, email, name, phone, userRole);
        }

        // Kiểm tra xem API hiện tại có bắt buộc đăng nhập không
        boolean isRequireLogin = parameter.getMethodAnnotation(RequireLogin.class) != null
                || parameter.getDeclaringClass().getAnnotation(RequireLogin.class) != null;

        // Nếu API cần đăng nhập mà userContext = null (token lỗi/thiếu) -> báo lỗi 401
        if (userContext == null && isRequireLogin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return userContext;
    }
}