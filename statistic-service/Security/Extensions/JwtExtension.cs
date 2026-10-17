
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using Microsoft.AspNetCore.Authorization;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using System.Text;
using StatisticService.Common.Configs;


namespace StatisticService.Security.Extensions;

public static class JwtExtension
{
    // Truyền IConfiguration vào đây để lấy biến môi trường
    public static IServiceCollection AddCustomJwtAuthentication(this IServiceCollection services, IConfiguration configuration)
    {
        // 1. Móc thẳng biến môi trường ra
        var envConfig = configuration.Get<EnvConfig>();

        if (string.IsNullOrEmpty(envConfig?.JWT_SECRET))
        {
            return services; // Nếu trống thì bỏ qua, hệ thống sẽ tự báo lỗi bên file AppConfigExtension
        }

        // 2. Cấu hình JWT
        var secretKey = Convert.FromBase64String(envConfig.JWT_SECRET);

        services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.MapInboundClaims = false; // Bắt buộc để giữ nguyên tên Role từ Java
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(secretKey),
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    RoleClaimType = "roles",
                    NameClaimType = "userEmail"
                };
            });

        return services;
    }
}