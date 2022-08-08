using EucyonBookIt.Models.Exceptions;
using EucyonBookIt.Models.MailModels;
using EucyonBookIt.Services.Interfaces;
using HotelManagementTestingProject.Helpers;

namespace HotelManagementTestingProject.EmailTests
{
    public class EmailIntegrationTests
    {
        private readonly IEmailService _service;

        public EmailIntegrationTests()
        {
            _service = TestingEmailServiceProvider.CreateEmailService();
        }

        [Fact]
        public void SendMimeMessageReturnsTrueWhenSmtpProcessesOk()
        {
            //Arrange
            var name = "Jenda Smith";
            var emailAddress = "my-address@koteb.nt";
            var newPassword = "Nf4QwqhPyGwOVJ1VnCr8hFoz";
            var message = new MimePasswordReset(name, emailAddress, newPassword);

            var result = _service.Send(message);

            Assert.True(result);
        }

        [Fact]
        public void SendMimeMessageReturnsExceptionWhenNoEmailAddressProvided()
        {
            //Arrange
            var name = "Jenda Smith";
            var emailAddress = "";
            var newPassword = "Nf4QwqhPyGwOVJ1VnCr8hFoz";
            var message = new MimePasswordReset(name, emailAddress, newPassword);
            
            //Act-Assert
            Assert.Throws<EmailServiceException>(() => _service.Send(message));
        }

        [Fact]
        public void SendManualReturnsTrueWhenSmtpProcessesOk()
        {
            //Arrange
            var name = "Jenda Smith";
            var emailAddress = "my-address@koteb.nt";
            var subject = "IntegrationTest";
            var body = "Test name: SendManualReturnsTrueWhenSmtpProcessesOk";

            //Act
            var result = _service.Send(name, emailAddress, subject, body);

            //Assert
            Assert.True(result);
        }

        [Fact]
        public void SendManualReturnsExceptionWhenNoEmailAddressProvided()
        {
            //Arrange
            var name = "Jenda Smith";
            var emailAddress = "";
            var subject = "IntegrationTest";
            var body = "Test name: SendManualReturnsTrueWhenSmtpProcessesOk";

            //Act-Assert
            Assert.Throws<EmailServiceException>(() => _service.Send(name, emailAddress, subject, body));
        }
    }
}
