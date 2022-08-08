using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;

namespace HotelManagementTestingProject.Helpers
{
    public static class TestingEmailServiceProvider
    {
        public static IEmailService CreateEmailService()
        {
            EnvVariablesMapper.SetSecretsAsEnvVariables();
            return new EmailServiceMailtrap();
        }
    }
}
