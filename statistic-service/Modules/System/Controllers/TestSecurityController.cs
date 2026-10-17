using Microsoft.AspNetCore.Mvc;
using StatisticService.Common.Responses;
using StatisticService.Security.Attributes;
using StatisticService.Security.DTOs; // Nhớ import UserContext

namespace StatisticService.Modules.System.Controllers;

[ApiController]
[Route("api/v1/statistics/test-security")]
public class TestSecurityController : ControllerBase
{
    // 🌟 KHÔNG CẦN CONSTRUCTOR NỮA! QUÊN ICURRENTUSERSERVICE ĐI!

    [HttpGet("public")]
    public IActionResult PublicEndpoint()
    {
        return Ok(ApiResponse<object>.Success(
            data: "Public", 
            message: "Khách vãng lai cũng có thể xem được."
        ));
    }

    [HttpGet("protected")]
    [RequireLogin] // Tương đương @RequireLogin
    // 🌟 Chỉ cần gắn [CurrentUser] vào tham số, .NET sẽ tự bơm dữ liệu vào!
    public IActionResult ProtectedEndpoint([CurrentUser] UserContext user)
    {
        return Ok(ApiResponse<object>.Success(
            data: user, 
            message: "Bạn đã đăng nhập hợp lệ!"
        ));
    }

    [HttpGet("admin-or-mod")]
    [IsAdminOrModerator]
    public IActionResult AdminOrModEndpoint([CurrentUser] UserContext user)
    {
        return Ok(ApiResponse<object>.Success(
            data: user, 
            message: "Chào mừng Quản trị viên/Người kiểm duyệt!"
        ));
    }

    [HttpGet("admin-only")]
    [IsAdmin]
    public IActionResult AdminOnlyEndpoint([CurrentUser] UserContext user)
    {
        return Ok(ApiResponse<object>.Success(
            data: user, 
            message: "Tuyệt vời! Bạn là ADMIN hệ thống."
        ));
    }
}