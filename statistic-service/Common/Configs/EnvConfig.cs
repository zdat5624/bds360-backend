using System.ComponentModel.DataAnnotations;

namespace StatisticService.Common.Configs;

public class EnvConfig
{
    // ==========================================
    // SHARED SECRETS (Tự động nạp từ file .env qua Docker)
    // ==========================================
    [Required(ErrorMessage = "❌ Không tìm thấy JWT_SECRET trong môi trường!")]
    [MinLength(32, ErrorMessage = "❌ Chuỗi JWT_SECRET phải dài ít nhất 32 ký tự.")]
    public string JWT_SECRET { get; init; } = string.Empty;

    // ==========================================
    // GRPC URLS (Cấu hình cứng tạm thời để gọi nội bộ)
    // ==========================================
    public string GRPC_IDENTITY_URL { get; init; } = "http://identity-service:9091";
    public string GRPC_LISTING_URL { get; init; } = "http://listing-service:9092";
    public string GRPC_PAYMENT_URL { get; init; } = "http://payment-service:9094";
}