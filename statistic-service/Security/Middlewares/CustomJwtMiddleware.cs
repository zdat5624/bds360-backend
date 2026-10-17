using System.Text.Json;
using StatisticService.Common.Exceptions;
using StatisticService.Common.Responses;

namespace StatisticService.Security.Middlewares;

public class CustomJwtMiddleware
{
    private readonly RequestDelegate _next;

    public CustomJwtMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task Invoke(HttpContext context)
    {
        await _next(context);

        // Nếu đã có dữ liệu xuất ra rồi thì thôi không can thiệp
        if (context.Response.HasStarted) return;

        if (context.Response.StatusCode == StatusCodes.Status401Unauthorized)
        {
            context.Response.ContentType = "application/json";
            var response = ApiResponse<object>.Error(ErrorCode.UNAUTHORIZED.Code, ErrorCode.UNAUTHORIZED.Message);
            var options = new JsonSerializerOptions { PropertyNamingPolicy = JsonNamingPolicy.CamelCase };
            await context.Response.WriteAsJsonAsync(response, options);
        }
        else if (context.Response.StatusCode == StatusCodes.Status403Forbidden)
        {
            context.Response.ContentType = "application/json";
            var response = ApiResponse<object>.Error(ErrorCode.FORBIDDEN.Code, ErrorCode.FORBIDDEN.Message);
            var options = new JsonSerializerOptions { PropertyNamingPolicy = JsonNamingPolicy.CamelCase };
            await context.Response.WriteAsJsonAsync(response, options);
        }
    }
}