using Microsoft.AspNetCore.Mvc;
using System.Text.Json.Serialization; // 🌟 Bổ sung thư viện này ở đầu file
using StatisticService.Common.Configs;
using StatisticService.Common.Exceptions;
using StatisticService.Security.Extensions;
using StatisticService.Security.Middlewares;

var builder = WebApplication.CreateBuilder(args);

builder.Services.Configure<ApiBehaviorOptions>(options => options.SuppressModelStateInvalidFilter = true);

builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        // serialize enum thành string, tránh trả về số
        options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
    });

builder.Services.AddExceptionHandler<GlobalExceptionHandler>();
builder.Services.AddProblemDetails();

// 1. Load và check file .env
builder.Services.AddAppConfigurations(builder.Configuration);

// 2. Setup JWT (Truyền cấu hình vào đây)
builder.Services.AddCustomJwtAuthentication(builder.Configuration);

// 3. Setup Services
builder.Services.AddHttpContextAccessor();

var app = builder.Build();

app.UseExceptionHandler();
app.UseRouting();
app.UseMiddleware<CustomJwtMiddleware>();
app.UseAuthentication();
app.UseAuthorization();
app.MapControllers();

app.Run();