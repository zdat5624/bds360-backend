namespace StatisticService.Common.Exceptions;

public record ErrorCode(int Code, int Status, string Message)
{
    // ==========================================
    // 10. COMMON & SYSTEM (Core)
    // ==========================================
    public static readonly ErrorCode INTERNAL_ERROR = new(10001, 500, "Sự cố hệ thống máy chủ.");
    public static readonly ErrorCode VALIDATION_ERROR = new(10002, 400, "Dữ liệu đầu vào không hợp lệ.");
    public static readonly ErrorCode INVALID_PARAMETER = new(10003, 400, "Tham số truyền vào thiếu hoặc không hợp lệ.");
    public static readonly ErrorCode UNAUTHORIZED = new(10004, 401, "Chưa xác thực hoặc token không hợp lệ.");
    public static readonly ErrorCode FORBIDDEN = new(10005, 403, "Bạn không có quyền truy cập vào tài nguyên này.");
    public static readonly ErrorCode API_NOT_FOUND = new(10006, 404, "Đường dẫn API không tồn tại trên hệ thống.");
    public static readonly ErrorCode METHOD_NOT_ALLOWED = new(10007, 405, "Phương thức HTTP không được hỗ trợ.");


    public static readonly ErrorCode GRPC_CONNECTION_FAILED = new(10012, 503, "Không thể kết nối tới các dịch vụ nội bộ.");
}