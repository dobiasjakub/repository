using EucyonBookIt.Services.Interfaces;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace HotelManagementTestingProject.Helpers
{
    static class GetTestingJWTToken
    {

        public static string GetToken(string id, string userName, string role)
        {
            EnvVariablesMapper.SetSecretsAsEnvVariables();
            IAuthService _authService = TestingAuthServiceProvider.GetTestingAuthService();

            var claims = new Claim[]
            {
                new Claim("Id", id),
                new Claim(ClaimTypes.Name, userName),
                new Claim(ClaimTypes.Role, role)
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Environment.GetEnvironmentVariable("JwtSigningKey")));
            var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha512);
            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(1),
                signingCredentials: credentials);

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);
            return jwt;
        }
    }
}
