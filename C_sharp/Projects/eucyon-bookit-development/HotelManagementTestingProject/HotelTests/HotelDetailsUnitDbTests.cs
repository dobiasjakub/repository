using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using EucyonBookIt.Database;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;
using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;

namespace HotelManagementTestingProject.HotelTests
{
    public class HotelDetailsUnitDbTests
    {
        private readonly IHotelService _service;
        private readonly ApplicationContext _context;

        public HotelDetailsUnitDbTests()
        {
            _context = TestingContextProvider.CreateContextFromScratch();
            _service = new HotelService(_context, TestingMapperProvider.CreateAutomapper());
        }

        [Fact]
        public void GetHotelDetailsByIdReturnsDTOWithHotelDetails()
        {
            // Arrange
            long id = 1;

            var expectedHotel = new HotelDetailsDTO
            {
                HotelId = 1,
                Name = "Dummy Hotel",
                Location = "In Memory",
                Description = "Just a good ol' fake",
                Rooms = new List<RoomDetailsDTO>
                {
                    new RoomDetailsDTO
                    {
                        RoomId = 1,
                        HotelId = 1,
                        Description = "Best memory location for your 1s and 0s",
                        Capacity = 5,
                        Price = 3000,
                        IsActive = true
                    }
                },
                Reviews = new List<HotelReviewDTO>
                {
                    new HotelReviewDTO
                    {
                        Title = "Best stay in RAM",
                        Text = "Loved the stay, recommend!",
                        CreatedAt = new DateTime(2022-07-07-08),
                        Stars = 5,
                        ReservationId = 1,
                        HotelId = 1
                    }
                },
                Manager = new UserManagerDTO { EmailAddress = "MrManage@holidaywin.com" }
            };

            // Act
            var hotelDetails = _service.GetHotelDetailsById(id);

            // Assert
            Assert.Equal(expectedHotel.HotelId, hotelDetails.HotelId);
            Assert.Equal(expectedHotel.Name, hotelDetails.Name);
            Assert.Equal(expectedHotel.Description, hotelDetails.Description);
            Assert.Equal(expectedHotel.Rooms.Count, hotelDetails.Rooms.Count);
            Assert.Equal(expectedHotel.Reviews.Count, hotelDetails.Reviews.Count);
            Assert.Equal(expectedHotel.Manager.EmailAddress, hotelDetails.Manager.EmailAddress);
        }

        [Fact]
        public void GetHotelDetailsByLocationAndNameReturnsJsonFileWithExpectedHotelDetails()
        {
            // Arrange
            string location = "In Memory";
            string name = "dummy hotel";

            var expectedHotel = new HotelDetailsDTO
            {
                HotelId = 1,
                Name = "Dummy Hotel",
                Location = "In Memory",
                Description = "Just a good ol' fake",
                Rooms = new List<RoomDetailsDTO>
                {
                    new RoomDetailsDTO
                    {
                        RoomId = 1,
                        HotelId = 1,
                        Description = "Best memory location for your 1s and 0s",
                        Capacity = 5,
                        Price = 3000,
                        IsActive = true
                    }
                },
                Reviews = new List<HotelReviewDTO>
                {
                    new HotelReviewDTO
                    {
                        Title = "Best stay in RAM",
                        Text = "Loved the stay, recommend!",
                        CreatedAt = new DateTime(2022-07-07-08),
                        Stars = 5,
                        ReservationId = 1,
                        HotelId = 1
                    }
                },
                Manager = new UserManagerDTO { EmailAddress = "MrManage@holidaywin.com" }
            };

            // Act
            var hotelDetails = _service.GetHotelByLocationAndName(location, name);

            // Assert
            Assert.Equal(expectedHotel.HotelId, hotelDetails.HotelId);
            Assert.Equal(expectedHotel.Name, hotelDetails.Name);
            Assert.Equal(expectedHotel.Description, hotelDetails.Description);
            Assert.Equal(expectedHotel.Rooms.Count, hotelDetails.Rooms.Count);
            Assert.Equal(expectedHotel.Reviews.Count, hotelDetails.Reviews.Count);
            Assert.Equal(expectedHotel.Manager.EmailAddress, hotelDetails.Manager.EmailAddress);
        }

        [Fact]
        public void GetHotelDetailsByLocationReturnsExpectedCountOfHotelsInList()
        {
            // Arrange
            string location = "In Memory";
            int expectedCount = 3;

            // Act
            var hotels = _service.GetHotelsByLocation(location);

            // Assert
            Assert.Equal(hotels.Hotels.Count, expectedCount);
        }

        [Fact]
        public void GetHotelNamesByLocationReturnsExpectedCountOfHotelsInList()
        {
            // Arrange
            string location = "In Memory";
            int expectedCount = 3;

            // Act
            var hotels = _service.GetHotelNamesByLocation(location);

            // Assert
            Assert.Equal(hotels.HotelNames.Count, expectedCount);
        }

        [Fact]
        public void GetHotelsByLocationReturnsOrderedListOfHotelsByNameASC()
        { 
            // Arrange
            string location = "In Memory";

            // Act
            var hotels = _service.GetHotelsByLocation(location);
            List<string> hotelNames = new List<string>();

            foreach(var hotelDetailsDTO in hotels.Hotels)
            { 
                hotelNames.Add(hotelDetailsDTO.Name);
            }

            var sorted = new List<string>();
            sorted.AddRange(hotelNames.OrderBy(o => o));

            // Assert
            Assert.True(hotelNames.SequenceEqual(sorted));
        }

        [Fact]
        public void GetHotelNamesByLocationReturnsOrderedListByNameASC()
        {
            // Arrange
            string location = "In Memory";

            // Act
            var hotels = _service.GetHotelNamesByLocation(location);
            List<string> expectedNames = hotels.HotelNames;
            var sorted = new List<string>();
   
            sorted.AddRange(expectedNames.OrderBy(o => o));

            // Assert
            Assert.True(expectedNames.SequenceEqual(sorted));
        }
   
        [Theory]
        [InlineData("In Memory")]
        [InlineData("in memory")]
        [InlineData("IN MEMORY")]
        [InlineData("In meMORY")]
        [InlineData("In mEmOrY")]
        public void LocationCanBeSettedByUpperOrLowerCasesForMethodGetHotelNamesByLocation(string location)
        {
            // Arrange
            int expectedCount = 3;

            // Act
            var hotels = _service.GetHotelNamesByLocation(location);

            // Assert
            Assert.Equal(hotels.HotelNames.Count, expectedCount);
        }

        [Theory]
        [InlineData("In Memory")]
        [InlineData("in memory")]
        [InlineData("IN MEMORY")]
        [InlineData("In meMORY")]
        [InlineData("In mEmOrY")]
        public void LocationCanBeSettedByUpperOrLowerCasesForMethodGetHotelsByLocation(string location)
        {
            // Arrange
            int expectedCount = 3;

            // Act
            var hotels = _service.GetHotelsByLocation(location);

            // Assert
            Assert.Equal(hotels.Hotels.Count, expectedCount);
        }
    }
}
