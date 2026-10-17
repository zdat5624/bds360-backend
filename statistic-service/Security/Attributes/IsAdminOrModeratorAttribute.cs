using Microsoft.AspNetCore.Authorization;
using StatisticService.Common.Constants;

namespace StatisticService.Security.Attributes;

public class IsAdminOrModeratorAttribute : AuthorizeAttribute
{
    public IsAdminOrModeratorAttribute()
    {
        Roles = $"{nameof(Role.ADMIN)},{nameof(Role.MODERATOR)}";
    }
}