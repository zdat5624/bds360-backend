using Microsoft.AspNetCore.Authorization;
using StatisticService.Common.Constants;

namespace StatisticService.Security.Attributes;

public class IsAdminAttribute : AuthorizeAttribute
{
    public IsAdminAttribute()
    {
        Roles = nameof(Role.ADMIN);
    }
}