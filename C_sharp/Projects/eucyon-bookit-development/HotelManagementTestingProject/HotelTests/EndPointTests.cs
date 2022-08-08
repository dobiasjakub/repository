using EucyonBookIt.Models;
using HotelManagementTestingProject.Helpers;
using System.Net.Http.Headers;
using System.Text.Json;

namespace HotelManagementTestingProject.HotelTests
{
    public class EndPointTests : IClassFixture<CustomWebApplicationFactory>
    {
        private readonly CustomWebApplicationFactory _factory;

        public EndPointTests(CustomWebApplicationFactory customWebApplicationFactory)
        {
            _factory = customWebApplicationFactory;
            TestingContextProvider.CreateContextFromFactory(_factory);
        }

        [Fact]
        public async Task EndpointReturnsOkStatusCodeIfHotelExistsById()
        {
            // Arrange
            var expectedStatusCode = 200;

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/id/1");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointReturnsOkStatusCodeIfHotelExistsByName()
        {
            // Arrange
            var expectedStatusCode = 200;

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/locationAndName/In%20Memory/Dummy%20Hotel");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointReturnsErrorMessageAndNotFoundCodeIfHotelIsNotExistsById()
        {
            // Arrange
            var expectedError = new ErrorDetail { StatusCode = 404, Message = "Hotel not found" };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/id/0123456789");
            string body = await response.Content.ReadAsStringAsync();
            var resultError = JsonSerializer.Deserialize<ErrorDetail>(body, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

            // Assert
            Assert.Equal(expectedError.StatusCode, (int)response.StatusCode);
            Assert.Equal(expectedError.Message, resultError.Message);
        }

        [Fact]
        public async Task EndpointReturnsErrorMessageAndNotFoundCodeIfHotelIsNotExistsByNameAndLocation()
        {
            // Arrange
            var expectedError = new ErrorDetail { StatusCode = 404, Message = "Hotel not found" };

            string jwt = GetTestingJWTToken.GetToken("2","MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/LocationAndName/IAmNotExist/NietherDoI");
            string body = await response.Content.ReadAsStringAsync();
            var resultError = JsonSerializer.Deserialize<ErrorDetail>(body, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

            // Assert
            Assert.Equal(expectedError.StatusCode, (int)response.StatusCode);
            Assert.Equal(expectedError.Message, resultError.Message);
        }

        [Fact]
        public async Task EndpointGetHotelsByLocationReturnsOkStatusCodeIfHotelsAreExistsInLocation()
        {
            // Arrange
            var expectedStatusCode = 200;

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/hotels/location/In%20Memory/details");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointGetHotelNamesByLocationReturnsOkStatusCodeIfHotelsAreExistsInLocation()
        {
            // Arrange
            var expectedStatusCode = 200;

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/hotels/location/In%20Memory/names");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointGetHotelsWithDetailsByLocationReturnsErrorMessageAndNotFoundCodeIfHotelsAreNotExistsInDedicatedLocation()
        {
            // Arrange
            var expectedError = new ErrorDetail { StatusCode = 404, Message = "There are no hotels in dedicated location" };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/hotels/Location/Gornja%20Pripizdina/details");
            string body = await response.Content.ReadAsStringAsync();
            var resultError = JsonSerializer.Deserialize<ErrorDetail>(body, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

            // Assert
            Assert.Equal(expectedError.StatusCode, (int)response.StatusCode);
            Assert.Equal(expectedError.Message, resultError.Message);
        }

        [Fact]
        public async Task EndpointGetHotelNamesByLocationReturnsErrorMessageAndNotFoundCodeIfHotelsAreNotExistsInDedicatedLocation()
        {
            // Arrange
            var expectedError = new ErrorDetail { StatusCode = 404, Message = "There are no hotels in dedicated location" };
            var authnUser = new { EmailAddress = "MrManage@holidaywin.com", Password = "Password123" };

            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            // Act
            var response = await httpClient.GetAsync("api/hotel/hotels/Location/Gornja%20Pripizdina/names");
            string body = await response.Content.ReadAsStringAsync();
            var resultError = JsonSerializer.Deserialize<ErrorDetail>(body, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

            // Assert
            Assert.Equal(expectedError.StatusCode, (int)response.StatusCode);
            Assert.Equal(expectedError.Message, resultError.Message);
        }
    }
}
