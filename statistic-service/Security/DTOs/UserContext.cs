using StatisticService.Common.Constants;

namespace StatisticService.Security.DTOs;

public record UserContext(
    long Id,
    string Email,
    string Name,
    string Phone,
    Role Role
);