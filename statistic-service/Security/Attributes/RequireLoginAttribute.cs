using Microsoft.AspNetCore.Authorization;

namespace StatisticService.Security.Attributes;

public class RequireLoginAttribute : AuthorizeAttribute
{
    public RequireLoginAttribute()
    {
        // 🌟 Bổ sung dòng này để bắt buộc phải có tài khoản (y hệt isAuthenticated() của Java)
        Policy = string.Empty;
    }
}