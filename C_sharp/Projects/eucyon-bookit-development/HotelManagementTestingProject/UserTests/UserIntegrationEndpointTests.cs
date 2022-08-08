using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;
using System.Globalization;
using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace HotelManagementTestingProject.UserTests
{
    public class UserIntegrationEndpointTests : IClassFixture<CustomWebApplicationFactory>
    {
        private readonly CustomWebApplicationFactory _factory;
        
        public UserIntegrationEndpointTests(CustomWebApplicationFactory factory)
        {
            _factory = factory;
            TestingContextProvider.CreateContextFromFactory(_factory);
        }

        [Fact]
        public async Task LoginWithCorrectLoginCredentialsReturns200Response()
        {
            //Arrange
            var expectedDTO = new UserLoginSuccessDTO("Login successful", authToken: "Yeah, I don't think I can guess that");
            var expectedStatusCode = 200;

            var username = "my-address@koteb.nt";
            var password = "th3reMustAlw4ysBeAPassword";
            var loginDto = new UserLoginDTO 
            {
                EmailAddress = username,
                Password = password
            };

            //Act
            var response = await _factory.CreateClient().PostAsync("/api/user/login", JsonContent.Create(loginDto));
            var body = await response.Content.ReadAsStringAsync();
            var bodyDTO = JsonConvert.DeserializeObject<UserLoginSuccessDTO>(body);

            //Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(expectedDTO.Message, bodyDTO.Message);
            Assert.True(bodyDTO.AuthToken.Length > 150);
        }

        [Fact]
        public async Task LoginWithWrongLoginCredentialsReturns400Response()
        {
            //Arrange
            var expectedResponse = new BaseResponseDTO("Invalid combination of username and password");
            string expectedBody = JsonConvert.SerializeObject(expectedResponse);
            var expectedStatusCode = 400;

            var username = "non-existent@user.cz";
            var password = "Wr0ngPassw0rd";
            var loginDto = new UserLoginDTO
            {
                EmailAddress = username,
                Password = password
            };

            //Act
            var response = await _factory.CreateClient().PostAsync("/api/user/login", JsonContent.Create(loginDto));
            string body = await response.Content.ReadAsStringAsync();
            body = JsonConvert.SerializeObject(JsonConvert.DeserializeObject<BaseResponseDTO>(body));

            //Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(expectedBody, body);
        }

        [Fact]
        public async Task RegistrationAddsNewUserWhenCorrectInputProvided()
        {
            //Arrange
            var expectedResponse = new BaseResponseDTO("You are now registered");
            var expectedBody = JsonConvert.SerializeObject(expectedResponse);
            var expectedStatusCode = 200;

            var username = "email@adress.com";
            var password = "SuperSafePsw123";
            var role = "MANAGER";
            var registrationDto = new UserRegistrationDTO
            {
                EmailAddress = username,
                Password = password,
                PasswordRetype = password,
                Role = role
            };

            //Act
            var response = await _factory.CreateClient().PostAsync("/api/user/registration", JsonContent.Create(registrationDto));
            string body = await response.Content.ReadAsStringAsync();
            body = JsonConvert.SerializeObject(JsonConvert.DeserializeObject<BaseResponseDTO>(body));

            //Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(expectedBody, body);
        }

        [Fact]
        public async Task RegistrationThrowsExceptionOnIncorrectPasswordFormat()
        {
            //Arrange
            var expectedError = new ErrorDetail { StatusCode = 400, Message = "Invalid password format. Password needs to contain an uppercase letter, a lowercase letter and a number. Special characters are not permitted" };

            var username = "email@adress.com";
            var password = "12E456789";
            var role = "Customer";
            var registrationDto = new UserRegistrationDTO
            {
                EmailAddress = username,
                Password = password,
                PasswordRetype = password,
                Role = role
            };

            //Act
            var response = await _factory.CreateClient().PostAsync("/api/user/registration", JsonContent.Create(registrationDto));
            string body = await response.Content.ReadAsStringAsync();
            var errorDetail = JsonConvert.DeserializeObject<ErrorDetail>(body);

            //Assert
            Assert.Equal(expectedError.StatusCode, (int)response.StatusCode);
            Assert.Equal(expectedError.Message, errorDetail.Message);
        }

        [Fact]
        public async Task EditUserGETCorrectInputReturnEditableUser200Async()
        {
            // Arrange
            UserEditDTO expectedUser = new()
            {
                UserId = 2,
                EmailAddress = "MrManage@holidaywin.com",
                Password = "Password123",
                NewPassword = null,
                PersonFirstName = "Mr",
                PersonLastName = "Manage",
                RoleRoleName = "Manager"
            };
            int expectedStatusCode = 200;

            string jwt = GetTestingJWTToken.GetToken("2", expectedUser.EmailAddress, expectedUser.RoleRoleName);
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            //var response = await _factory.CreateClient().GetAsync($"/api/user");
            var response = await httpClient.GetAsync($"/api/user");
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(JsonConvert.SerializeObject(expectedUser),
                         JsonConvert.SerializeObject(JsonConvert.DeserializeObject<UserEditDTO>(actualBody)));
        }

        [Fact]
        public async Task EditUserGETIncorrectCredentialsReturnNotFound404Async()
        {
            // Arrange
            string roleName = "Manager";
            int expectedStatusCode = 404;

            string jwt = GetTestingJWTToken.GetToken("2", "MrManagefa@holidaywin.com", roleName);

            BaseResponseDTO expectedResponse = new BaseResponseDTO("User not found in DB.");

            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync($"/api/user");
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(JsonConvert.SerializeObject(expectedResponse), 
                         JsonConvert.SerializeObject(JsonConvert.DeserializeObject<BaseResponseDTO>(actualBody)));
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EditUserGETIncorrectTokenReturnUnauthorize401Async()
        {
            // Arrange
                    int expectedStatusCode = 401;

            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "");

            // Act
            var response = await httpClient.GetAsync($"/api/user");
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async void EditUserPUTCorrectInputReturnsUserPutDTOStatus200()
        {
            // Arrange
            UserEditDTO expectedUser = new()
            {
                UserId = 2,
                EmailAddress = "MrManage@holidaywin.com",
                Password = "Password123",
                NewPassword = null,
                PersonFirstName = "Mrs",
                PersonLastName = "Managee",
                RoleRoleName = "Manager"
            };

            int expectedStatusCode = 200;

            UserEditDTO editUser = new()
            {
                UserId = 2,
                EmailAddress = "MrManage@holidaywin.com",
                Password = "Password123",
                PersonFirstName = "Mrs",
                PersonLastName = "Managee",
                RoleRoleName = "Manager"
            };

            string jwt = GetTestingJWTToken.GetToken("2", expectedUser.EmailAddress, expectedUser.RoleRoleName);
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.PutAsync("/api/user/edit", JsonContent.Create(editUser));
            string body = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(JsonConvert.SerializeObject(expectedUser), 
                         JsonConvert.SerializeObject(JsonConvert.DeserializeObject<UserEditDTO>(body)));
        }

        [Fact]
        public async void EditUserPUTNullInputReturnsBadRequestStatus400()
        {
            // Arrange
            int expectedStatusCode = 400;
            UserEditDTO? editUser = null;

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.PutAsync("/api/user/edit", JsonContent.Create(editUser));
            _ = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async void EditUserPUTIncorrectCredentialsReturnsNotFoundStatus404()
        {
            // Arrange
            UserEditDTO expectedUser = new()
            {
                UserId = 2,
                EmailAddress = "MrManage@holidaywin.com",
                Password = "Password",
                NewPassword = null,
                PersonFirstName = "Mr",
                PersonLastName = "Manage",
                RoleRoleName = "Manager"
            };
            int expectedStatusCode = 401;

            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "");

            // Act
            var response = await httpClient.PutAsync("/api/user/edit", JsonContent.Create(expectedUser));
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async void EditUserPATCHIncorrectCredentialsdReturnsNotFoundStatus404()
        {
            // Arrange
            string userName = "MrManagefa@holidaywin.com";
            int expectedStatusCode = 404;
            
            BaseResponseDTO expectedResponse = new BaseResponseDTO("Incorrect username!");

            var userPatch = new[]
            {
                new{
                op = "replace",
                path = "/PersonFirstNameLastName",
                value = "Mrs"
                }
            };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.PatchAsync($"/api/user/edit/{userName}", JsonContent.Create(userPatch));
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(JsonConvert.SerializeObject(expectedResponse),
                         JsonConvert.SerializeObject(JsonConvert.DeserializeObject<BaseResponseDTO>(actualBody)));
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async void EditUserPATCHIncorrectPatchdReturnsBadRequestStatus400()
        {
            // Arrange
            string userName = "MrManage@holidaywin.com";
            int expectedStatusCode = 400;

            var userPatch = new[]
            {
                new{
                op = "replace",
                path = "/PersonFirstNameLastName",
                value = "Mrs"
                }
            };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.PatchAsync($"/api/user/edit/{userName}", JsonContent.Create(userPatch));
            _ = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async void EditUserPATCHPasswordPatchdReturnsBadRequestStatus400()
        {
            // Arrange
            BaseResponseDTO expectedResponse = new BaseResponseDTO("Edit Password with PUT method only!");
            int expectedStatusCode = 400;

            var userPatch = new[]
            {
                new{
                op = "replace",
                path = "/Password",
                value = "TomasFuk"
                }
            };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.PatchAsync($"/api/user/edit/MrManage@holidaywin.com", JsonContent.Create(userPatch));
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(JsonConvert.SerializeObject(expectedResponse),
                         JsonConvert.SerializeObject(JsonConvert.DeserializeObject<BaseResponseDTO>(actualBody)));
        }

        [Fact]
        public async void EditUserPATCHCorrectPatchdReturnsStatus200()
        {
            // Arrange
            UserEditDTO expectedUser = new()
            {
                UserId = 2,
                EmailAddress = "MrManage@holidaywin.com",
                Password = "Password123",
                NewPassword = null,
                PersonFirstName = "Mrs",
                PersonLastName = "Manage",
                RoleRoleName = "Manager"
            };
            int expectedStatusCode = 200;

            var userPatch = new[]
            {
                new{
                op = "replace",
                path = "/PersonFirstName",
                value = "Mrs"
                }
            };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.PatchAsync($"/api/user/edit/{expectedUser.EmailAddress}", JsonContent.Create(userPatch));
            string actualBody = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(JsonConvert.SerializeObject(expectedUser), 
                         JsonConvert.SerializeObject(JsonConvert.DeserializeObject<UserEditDTO>(actualBody)));
        }

        [Fact]
        public async Task ResetPasswordGeneratesNewPasswordOnExistingEmailAddressInput()
        {
            //Arrange
            var expectedResponse = new BaseResponseDTO("Password reset. Check your mail.");
            var expectedBody = JsonConvert.SerializeObject(expectedResponse);

            var emailAddress = "my-address@koteb.nt";
            var dto = new ResetPasswordDTO
            {
                EmailAddress = emailAddress
            };

            //Act
            var response = await _factory.CreateClient().PostAsync("api/user/resetpassword", JsonContent.Create(dto));
            string body = await response.Content.ReadAsStringAsync();
            body = JsonConvert.SerializeObject(JsonConvert.DeserializeObject<BaseResponseDTO>(body));

            //Assert
            Assert.Equal(expectedBody, body);
        }

        [Fact]
        public async Task ResetPasswordReturns400OnNonexistingEmailAddressInput()
        {
            //Arrange
            var expectedStatusCode = 400;
            var emailAddress = "non-existing@address.ca";
            var dto = new ResetPasswordDTO
            {
                EmailAddress = emailAddress
            };

            //Act
            var response = await _factory.CreateClient().PostAsync("api/user/resetpassword", JsonContent.Create(dto));

            //Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointReturnsLocalizedResponse()
        {
            //Arrange
            var expectedDTO = new UserLoginSuccessDTO("Přihlášení úspěšné", authToken: "Yeah, I don't think I can guess that");
            var expectedStatusCode = 200;

            var username = "MrManage@holidaywin.com";
            var password = "Password123";
            var loginDto = new UserLoginDTO
            {
                EmailAddress = username,
                Password = password
            };

            var client = _factory.CreateClient();
            client.DefaultRequestHeaders.Add("Accept-Language", "cs");

            //Act
            var response = await client.PostAsync("/api/user/login", JsonContent.Create(loginDto));
            var body = await response.Content.ReadAsStringAsync();
            var bodyDTO = JsonConvert.DeserializeObject<UserLoginSuccessDTO>(body);

            //Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
            Assert.Equal(expectedDTO.Message, bodyDTO.Message);
            Assert.NotNull(bodyDTO.AuthToken);
            //G is shortcut for long date/time format
            Assert.True(DateTime.TryParseExact(bodyDTO.DateIssued, "G", new CultureInfo("cs"), DateTimeStyles.AssumeLocal, out DateTime result));
        }
    }
}
