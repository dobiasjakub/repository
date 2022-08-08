using HotelManagementTestingProject.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace HotelManagementTestingProject.ManagerTests
{
    public class ManagerIntegrationEndpointTests : IClassFixture<CustomWebApplicationFactory>
    {
        private readonly CustomWebApplicationFactory _factory;

        public ManagerIntegrationEndpointTests(CustomWebApplicationFactory customWebApplicationFactory)
        {
            _factory = customWebApplicationFactory;
            TestingContextProvider.CreateContextFromFactory(_factory);
        }

        [Fact]
        public async Task EndpointForReservationsReturns401IfUserIsNotAuthorized()
        {   
            // Arrange
            var expectedStatusCode = 401;

            // Act
            var response = await _factory.CreateClient().GetAsync("api/manager/reservations");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointForReservationsReturnsOkStatusCodeIfManagerExistsByEmail()
        {
            // Arrange
            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);
            
            int expectedStatusCode = 200;

            // Act
            var response = await httpClient.GetAsync("api/manager/reservations/");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointForReservationsReturns403tatusCodeIfUserIsNoManager()
        {
            // Arrange
            string jwt = GetTestingJWTToken.GetToken("1", "my-address@koteb.nt", "Customer");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            int expectedStatusCode = 403;

            // Act
            var response = await httpClient.GetAsync("api/manager/reservations/");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointForReservationsByHotelsReturns401IfUserIsNotAuthorized()
        {
            // Arrange
            var expectedStatusCode = 401;
            
            // Act
            var response = await _factory.CreateClient().GetAsync("api/manager/reservations/hotels");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointForReservationsByHotelsReturnsOkStatusCodeIfManagerExistsByEmail()
        {
            //Arrange
            string jwt = GetTestingJWTToken.GetToken("2", "MrManage@holidaywin.com", "Manager");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            int expectedStatusCode = 200;

            // Act
            var response = await httpClient.GetAsync("api/manager/reservations/hotels");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }

        [Fact]
        public async Task EndpointForReservationsByHotelsReturns403tatusCodeIfUserIsNoManager()
        {
            //Arrange
            string jwt = GetTestingJWTToken.GetToken("1", "my-address@koteb.nt", "Customer");
            var httpClient = _factory.CreateClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);

            int expectedStatusCode = 403;

            // Act
            var response = await httpClient.GetAsync("api/manager/reservations/hotels");

            // Assert
            Assert.Equal(expectedStatusCode, (int)response.StatusCode);
        }
    }
}

