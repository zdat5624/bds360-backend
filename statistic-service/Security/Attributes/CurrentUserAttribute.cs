using Microsoft.AspNetCore.Mvc;
using StatisticService.Security.Resolvers;

namespace StatisticService.Security.Attributes;

[AttributeUsage(AttributeTargets.Parameter)]
public class CurrentUserAttribute : ModelBinderAttribute
{
    public CurrentUserAttribute()
    {
        BinderType = typeof(CurrentUserModelBinder);
    }
}