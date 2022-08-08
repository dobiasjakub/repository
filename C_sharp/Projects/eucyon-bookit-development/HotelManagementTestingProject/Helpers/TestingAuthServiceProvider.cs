using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Configuration;

namespace HotelManagementTestingProject.Helpers
{
    public static class TestingAuthServiceProvider
    {
        public static IAuthService GetTestingAuthService()
        {
            return new AuthService();
        }
    }
}
