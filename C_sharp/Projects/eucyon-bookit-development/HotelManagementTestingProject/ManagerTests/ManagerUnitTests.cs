using EucyonBookIt.Database;
using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HotelManagementTestingProject.ManagerTests
{
    public class ManagerUnitTests
    {
        private readonly IManagerService _service;
        private readonly ApplicationContext _context;

        public ManagerUnitTests()
        {
            _context = TestingContextProvider.CreateContextFromScratch();
            _service = new ManagerService(_context, TestingMapperProvider.CreateAutomapper());
        }

        [Fact]
        public void GetReservationsCreatesDTOWithReservationsByManagerEmail()
        {
            // Arrange
            string email = "MrManage@holidaywin.com";
            int expectedCount = 3;
        
            // Act
            var reservations = _service.GetHotelReservationsByManagerEmail(email);
        
            // Assert
            Assert.Equal(reservations.Count, expectedCount);
        }

        [Fact]
        public void GetReservationsByHotelsCreatesDTOWithValidCountnOfManagedHotels()
        {
            // Arrange
            string email = "MrManage@holidaywin.com";
            int expectedCountOfHotels = 3;

            // Act
            var reservations = _service.GetReservationsByHotelsByManagerEmail(email);

            // Assert
            Assert.Equal(reservations.Count, expectedCountOfHotels);
        }
    } 
}
