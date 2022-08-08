using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;
using System.Globalization;

namespace HotelManagementTestingProject.UserTests
{
    public class UserUnitTests
    {
        private readonly IUserService _service;

        public UserUnitTests()
        {
            _service = new UserService(TestingContextProvider.CreateEmptyMockContext().Object, 
                TestingMapperProvider.CreateAutomapper(), 
                TestingEmailServiceProvider.CreateEmailService(),
                TestingLocalizerProvider.GetLocalizer());
        }

        [Theory]
        [InlineData("testing-email@withnotld")]
        [InlineData("@tooshortuserpart.com")]
        [InlineData("inv#lid@characters.de")]
        [InlineData("tld@tooshort.x")]
        [InlineData("numbers#intld.3com")]
        [InlineData("c@u")]
        public void EmailValidationCatchesInvalidAddresses(string emailAddress)
        {
            //Act
            var result = _service.ValidateEmailAddress(emailAddress, out string returnMessage);

            //Assert
            Assert.False(result);
        }

        [Theory]
        [InlineData("plain-old@address.com")]
        [InlineData("totally.valid@email.address.com")]
        [InlineData("s%t1LL@verymuch.ok")]
        [InlineData("v@l.id")]
        public void EmailValidationAllowsValidEmailAddress(string emailAddress)
        {
            //Act
            var result = _service.ValidateEmailAddress(emailAddress, out string returnMessage);

            //Assert
            Assert.True(result);
        }

        [Theory]
        [InlineData("Password123", "Crossword478")]
        public void PasswordValidationCatchesPasswordsInputsNotBeingTheSame(string password, string passwordRetype)
        {
            //Arrange
            string expectedMessage = "Passwords don't match";

            //Act
            var result = _service.ValidatePassword(password, passwordRetype, out string returnMessage);

            //Assert
            Assert.False(result);
            Assert.Equal(expectedMessage, returnMessage);
        }

        [Theory]
        [InlineData("Pass1", "Pass1")]
        [InlineData("ThisIsAMonstrousPasswordThatShouldNotBeAllowed", "ThisIsAMonstrousPasswordThatShouldNotBeAllowed")]
        public void PasswordValidationCatchesInvalidLength(string password, string passwordRetype)
        {
            //Arrange
            string expectedMessage = "Password doesn't meet required length criteria";

            //Act
            var result = _service.ValidatePassword(password, passwordRetype, out string returnMessage);

            //Assert
            Assert.False(result);
            Assert.Equal(expectedMessage, returnMessage);
        }

        [Theory]
        [InlineData("helloIamapassword", "helloIamapassword")]
        [InlineData("12E456789", "12E456789")]
        [InlineData("Iam$peci4l", "Iam$peci4l")]
        public void PasswordValidationCatchesInvalidPasswordFormat(string password, string passwordRetype)
        {
            //Arrange
            string expectedMessage = "Invalid password format. Password needs to contain an uppercase letter, a lowercase letter and a number. Special characters are not permitted";

            //Act
            var result = _service.ValidatePassword(password, passwordRetype, out string returnMessage);

            //Assert
            Assert.False(result);
            Assert.Equal(expectedMessage, returnMessage);
        }

        [Theory]
        [InlineData("Password123", "Password123")]
        [InlineData("sh0Rty", "sh0Rty")]
        public void PasswordValidationAllowsValidPasswords(string password, string passwordRetype)
        {
            //Arrange
            string expectedMessage = "Validation OK";

            //Act
            var result = _service.ValidatePassword(password, passwordRetype, out string returnMessage);

            //Assert
            Assert.True(result);
            Assert.Equal(expectedMessage, returnMessage);
        }

        [Fact]
        public void GenerateNewPasswordReturnsValidPassword()
        {
            //Arrange
            string expectedMessage = "Validation OK";

            //Act
            string newPassword = _service.GenerateNewPassword();

            //Assert
            Assert.True(_service.ValidatePassword(newPassword, newPassword, out string returnMessage));
            Assert.Equal(expectedMessage, returnMessage);
        }

        [Fact]
        public void GetValidationMessageInCzech()
        {
            //Arrange
            string expectedMessage = "Neplatný formát e-mailové adresy";
            string emailAddress = "inv#lid@characters.de";

            CultureInfo.CurrentUICulture = new CultureInfo("cs");

            //Act
            bool result = _service.ValidateEmailAddress(emailAddress, out string message);

            //Assert
            Assert.False(result);
            Assert.Equal(expectedMessage, message);
        }
    }
}
