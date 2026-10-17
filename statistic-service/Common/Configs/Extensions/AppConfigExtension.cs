using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;

namespace StatisticService.Common.Configs;

public static class AppConfigExtension
{
    public static IServiceCollection AddAppConfigurations(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddOptions<EnvConfig>()
            .Bind(configuration)
            .ValidateDataAnnotations()
            .ValidateOnStart();

        return services;
    }
}