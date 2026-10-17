using System.Security.Claims;
using System.Text.Json;
using Microsoft.AspNetCore.Mvc.ModelBinding;
using StatisticService.Common.Constants;
using StatisticService.Security.DTOs;

namespace StatisticService.Security.Resolvers;

public class CurrentUserModelBinder : IModelBinder
{
    public Task BindModelAsync(ModelBindingContext bindingContext)
    {
        if (bindingContext == null)
        {
            throw new ArgumentNullException(nameof(bindingContext));
        }

        // 🌟 Lấy User trực tiếp từ HttpContext của Request đang tới
        var user = bindingContext.HttpContext.User;

        if (user?.Identity == null || !user.Identity.IsAuthenticated)
        {
            // Trả về null nếu chưa đăng nhập (để các API public xử lý hoặc RequireLogin chặn)
            bindingContext.Result = ModelBindingResult.Success(null);
            return Task.CompletedTask;
        }

        // Logic bóc tách dữ liệu từ Token giống y hệt lúc trước
        var idStr = user.FindFirst("userId")?.Value;
        long.TryParse(idStr, out long id);

        var email = user.FindFirst("userEmail")?.Value ?? user.FindFirst("sub")?.Value ?? "";
        var name = user.FindFirst("userName")?.Value ?? "";
        var phone = user.FindFirst("userPhone")?.Value ?? "";

        Role userRole = Role.USER;

        var rolesClaim = user.FindFirst("roles")?.Value;
        if (!string.IsNullOrEmpty(rolesClaim))
        {
            try
            {
                using var jsonDoc = JsonDocument.Parse(rolesClaim);
                if (jsonDoc.RootElement.ValueKind == JsonValueKind.Array && jsonDoc.RootElement.GetArrayLength() > 0)
                {
                    var firstRole = jsonDoc.RootElement[0].GetString() ?? "";
                    if (firstRole.StartsWith("ROLE_")) firstRole = firstRole.Substring(5);
                    if (Enum.TryParse(firstRole, out Role parsedRole)) userRole = parsedRole;
                }
            }
            catch (JsonException)
            {
                var roleStr = rolesClaim;
                if (roleStr.StartsWith("ROLE_")) roleStr = roleStr.Substring(5);
                Enum.TryParse(roleStr, out userRole);
            }
        }

        var userContext = new UserContext(id, email, name, phone, userRole);

        // Trả kết quả thành công để C# tự động tiêm vào tham số của Controller
        bindingContext.Result = ModelBindingResult.Success(userContext);

        return Task.CompletedTask;
    }
}