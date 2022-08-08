using EucyonBookIt.Models;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;

namespace HotelManagementTestingProject.AuthTests
{
    public class AuthUnitTest
    {
        private readonly IAuthService _service;

        public AuthUnitTest()
        {
            EnvVariablesMapper.SetSecretsAsEnvVariables();
            _service = TestingAuthServiceProvider.GetTestingAuthService();
        }

        [Fact]
        public void CreateTokenReturnsJwtTokenWithCorrectClaims()
        {
            //Arrange
            var user = new User()
            {
                EmailAddress = "testing-email@address.com",
                Role = new Role()
                {
                    RoleName = "TestingRole"
                }
            };
            var emailClaim = new Claim(ClaimTypes.Name, user.EmailAddress);
            var roleClaim = new Claim(ClaimTypes.Role, user.Role.RoleName);

            //Act
            var result = _service.CreateToken(user);

            //Assert
            Assert.NotNull(result);
            var jwt = new JwtSecurityTokenHandler().ReadJwtToken(result);

            Assert.Contains(jwt.Claims, x => x.Value.Equals(emailClaim.Value));
            Assert.Contains(jwt.Claims, x => x.Value.Equals(roleClaim.Value));
        }
    }
}
