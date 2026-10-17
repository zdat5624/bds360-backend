using System.Text.Json;
using Grpc.Core;
using Microsoft.AspNetCore.Diagnostics;
using StatisticService.Common.Responses;

namespace StatisticService.Common.Exceptions;

public class GlobalExceptionHandler : IExceptionHandler
{
    private readonly ILogger<GlobalExceptionHandler> _logger;

    public GlobalExceptionHandler(ILogger<GlobalExceptionHandler> logger)
    {
        _logger = logger;
    }

    public async ValueTask<bool> TryHandleAsync(
        HttpContext httpContext,
        Exception exception,
        CancellationToken cancellationToken)
    {
        httpContext.Response.ContentType = "application/json";

        ErrorCode errorCode;

        switch (exception)
        {
            // 1. Lỗi do mình chủ động throw (Nghiệp vụ)
            case AppException appEx:
                errorCode = appEx.ErrorCode;
                break;

            // 2. Lỗi gọi gRPC thất bại (Service Java chết / Mất mạng)
            case RpcException rpcEx:
                _logger.LogError(rpcEx, "Lỗi kết nối gRPC nội bộ");
                errorCode = ErrorCode.GRPC_CONNECTION_FAILED;
                break;

            // 3. Lỗi chưa bắt (Lỗi hệ thống 500)
            default:
                _logger.LogError(exception, "Lỗi hệ thống (500)");
                errorCode = ErrorCode.INTERNAL_ERROR;
                break;
        }

        httpContext.Response.StatusCode = errorCode.Status;

        var response = ApiResponse<object>.Error(errorCode.Code, errorCode.Message);

        var options = new JsonSerializerOptions { PropertyNamingPolicy = JsonNamingPolicy.CamelCase };
        await httpContext.Response.WriteAsJsonAsync(response, options, cancellationToken);


        return true;
    }
}