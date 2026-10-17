namespace StatisticService.Common.Constants;

public enum ListingType
{
    RENT, // Cho thuê
    SALE  // Bán
}

public enum NotificationType
{
    POST,           // Thông báo liên quan về post
    TRANSACTION,    // Thông báo transaction
    SYSTEM_ALERT    // Thông báo từ hệ thống
}

public enum Role
{
    ADMIN,
    MODERATOR,
    USER
}

public enum TransactionStatus
{
    PENDING,
    SUCCESS,
    FAILED,
    CANCELLED
}