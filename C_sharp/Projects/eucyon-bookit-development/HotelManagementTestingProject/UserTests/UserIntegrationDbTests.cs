using EucyonBookIt.Database;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;
using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;
using Newtonsoft.Json;

namespace HotelManagementTestingProject.UserTests
{
    public class UserIntegrationDbTests
    {
        private readonly IUserService _service;
        private readonly ApplicationContext _context;

        public UserIntegrationDbTests()
        {
            _context = TestingContextProvider.CreateContextFromScratch();
            _service = new UserService(_context, 
                TestingMapperProvider.CreateAutomapper(), 
                TestingEmailServiceProvider.CreateEmailService(),
                TestingLocalizerProvider.GetLocalizer());
        }

        [Fact]
        public void LoginUserReturnsUserOnCorrectInput()
        {
            //Arrange
            var loginDto = new UserLoginDTO
            {
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword"
            };
            var expectedUser = new User
            {
                UserId = 1,
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword",
                RoleId = 1,
                IsActive = true,
                ManagedHotels = null,
                PersonId = 1,
                Reservations = new List<Reservation> { }
            };

            //Act
            var user = _service.LoginUser(loginDto);

            //Assert
            Assert.Equal(expectedUser.EmailAddress, user.EmailAddress);
            Assert.Equal(expectedUser.RoleId, user.RoleId);
        }

        [Fact]
        public void RegisterUser()
        {
            //Arrange
            var registrationDto = new UserRegistrationDTO
            {
                EmailAddress = "ramza@beoulve.iv",
                Password = "try1ITagain",
                PasswordRetype = "try1ITagain",
                Role = "Customer"
            };
            var expectedUser = new User
            {
                EmailAddress = "ramza@beoulve.iv",
                Password = "try1ITagain",
                IsActive = true,
                RoleId = 1
            };

            //Act
            var user = _service.RegisterUser(registrationDto);
            
            //Assert
            Assert.Equal(expectedUser.EmailAddress, user.EmailAddress);
            Assert.Equal(expectedUser.Password, user.Password);
            Assert.True(user.IsActive);
            Assert.Equal(1, user.RoleId);
        }

        [Fact]
        public void GetUserByUsernameReturnsCorrectUserEvenWithDifferentCharacterCaseInput()
        {
            //Arrange
            string username = "My-ADDress@koteb.nt";
            var expectedUser = new User
            {
                UserId = 1,
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword",
                RoleId = 1,
                IsActive = true,
                ManagedHotels = null,
                PersonId = 1,
                Reservations = new List<Reservation> { }
            };

            //Act
            var user = _service.GetUserBy(username, false);

            //Assert
            Assert.Equal(expectedUser.EmailAddress, user.EmailAddress);
            Assert.Equal(expectedUser.Password, user.Password);
            Assert.Equal(expectedUser.RoleId, user.RoleId);
        }

        [Fact]
        public void GetUserByUsernameAndPasswordReturnsCorrectUser()
        {
            //Arrange
            string username = "MrManage@holidaywin.com";
            string password = "Password123";
            var expectedUser = new User
            {
                UserId = 2,
                EmailAddress = "MrManage@holidaywin.com",
                Password = "Password123",
                RoleId = 2,
                IsActive = true,
                Reservations = null,
                PersonId = 2,
                ManagedHotels = new List<Hotel> { }
            };

            //Act
            var user = _service.GetUserBy(username, password);

            //Assert
            Assert.Equal(expectedUser.EmailAddress, user.EmailAddress);
            Assert.Equal(expectedUser.Password, user.Password);
            Assert.Equal(expectedUser.RoleId, user.RoleId);
        }

        [Fact]
        public void GetUserByUsernameReturnsNullWhenNoneFound()
        {
            //Arrange
            string username = "non-existent@username.cz";

            //Act
            var user = _service.GetUserBy(username, false);

            //Assert
            Assert.Null(user);
        }

        [Fact]
        public void GetUserByUsernameAndPasswordReturnsNullOnIncorrectPasswordInput()
        {
            //Arrange
            string emailAddress = "my-address@koteb.nt";
            string password = "tESTINGpSW1";

            //Act
            var user = _service.GetUserBy(emailAddress, password);

            //Assert
            Assert.Null(user);
        }

        [Fact]
        public void AddUserAddsUserToDbAndActivates()
        {
            //Arrange
            var newUser = new User
            {
                EmailAddress = "username@asd.cz",
                Password = "TestingPsw1",
                IsActive = false,
                RoleId = 1,
                PersonId = 3,
            };
            var expectedCount = _context.Users.Count() + 1;

            //Act
            var user = _service.AddUser(newUser, true);

            //Assert
            Assert.True(user.IsActive);
            Assert.Equal(newUser.EmailAddress, user.EmailAddress);

            var resultCount = _context.Users.Count();
            Assert.Equal(expectedCount, resultCount);
        }

        [Fact]
        public void GetUserByUserIncludingPersonCorrectInputReturnsCorrectUser()
        {
            // Arrange
            User getUser = new User
            {
                UserId = 1,
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword",
                IsActive = true,
                RoleId = 1,
                PersonId = 1
            };

            Role expectedRole = new Role()
            {
                RoleId = 1,
                RoleName = "Customer",
                Users = null
            };
            Person expectedPerson = new Person
            {
                PersonId = 1,
                FirstName = "Jenda",
                LastName = "Smith",
                User = null
            };

            User expectedUser = new User
            {
                UserId = 1,
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword",
                IsActive = true,
                RoleId = 1,
                Role = expectedRole,
                PersonId = 1,
                Person = expectedPerson
            };

            // Act
            User actualUser = _service.GetUserBy(getUser);
            actualUser.Person.User = null;
            actualUser.Person.UserId = null;
            actualUser.Role.Users = null;
            
            string expected = JsonConvert.SerializeObject(expectedUser);
            string actual = JsonConvert.SerializeObject(actualUser);
            
            // Assert
            Assert.Equal(expected, actual);
        }

        [Fact]
        public void GetUserByUserIncludingPersonIncorrectInputReturnsNull()
        {
            // Arrange
            User getUser = new User
            {
                UserId = 0,
                PersonId = 1
            };

            // Act
            User? actualUser = _service.GetUserBy(getUser);

            // Assert
            Assert.Null(actualUser);
        }

        [Fact]
        public void GetUserByUserIncludingPersonNullInputReturnsNull()
        {
            // Arrange
            User? getUser = null;

            // Act
            User? actualUser = _service.GetUserBy(getUser);

            // Assert
            Assert.Null(actualUser);
        }

        [Fact]
        public void UpdateUserByUserCorrectInputReturnsTrue()
        {
            // Arrange
            User updateUser = new User
            {
                UserId = 1,
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword",
                IsActive = true,
                RoleId = 1,
                PersonId = 1,
                Person = new Person
                {
                    PersonId = 1,
                    FirstName = "Will",
                    LastName = "Smith",
                    UserId = 1,
                    User = null
                }
            };

            // Act
            bool expected = _service.UpdateUser(updateUser);

            // Assert
            Assert.True(expected);
        }

        [Fact]
        public void UpdateUserByUserIncorrectInputReturnsFalse()
        {
            // Arrange
            User updateUser = new User
            {
                EmailAddress = "my-address@koteb.nt",
                Password = "th3reMustAlw4ysBeAPassword",
                IsActive = true,
                RoleId = 1,
                PersonId = 1,
                Person = new Person
                {
                    PersonId = 1,
                    FirstName = "Will",
                    LastName = "Smith",
                    UserId = 1,
                    User = null
                }
            };

            // Act
            bool expected = _service.UpdateUser(updateUser);

            // Assert
            Assert.False(expected);
        }

        [Fact]
        public void UpdateUserByUserNullInputReturnsFalse()
        {
            // Arrange
            User updateUser = null;

            // Act
            bool expected = _service.UpdateUser(updateUser);

            // Assert
            Assert.False(expected);
        }

        public void ResetPasswordGeneratesAndSavesUserNewPassword()
        {
            //Arrange
            var dto = new ResetPasswordDTO
            {
                EmailAddress = "my-address@koteb.nt"
            };
            string originalPsw = _service.GetUserBy(dto.EmailAddress).Password;

            //Act
            var editedUser = _service.ResetPassword(dto);

            //Assert
            Assert.NotEqual(originalPsw, editedUser.Password);
        }
    }
}
