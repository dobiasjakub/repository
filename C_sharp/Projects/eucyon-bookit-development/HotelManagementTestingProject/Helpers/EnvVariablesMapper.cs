using Microsoft.Extensions.Configuration;

namespace HotelManagementTestingProject.Helpers
{
    public static class EnvVariablesMapper
    {
        public static void SetSecretsAsEnvVariables()
        {
            var configuration = new ConfigurationBuilder().AddUserSecrets<Program>().Build();
            foreach (var child in configuration.GetChildren())
            {
                Environment.SetEnvironmentVariable(child.Key, child.Value);
            }
        }
    }
}
