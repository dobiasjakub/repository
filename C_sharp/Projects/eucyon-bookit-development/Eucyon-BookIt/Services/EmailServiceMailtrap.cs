using EucyonBookIt.Models.MailModels;
using EucyonBookIt.Services.Abstracts;
using MimeKit;

namespace EucyonBookIt.Services
{
    public class EmailServiceMailtrap : EmailServiceBase
    {
        public EmailServiceMailtrap()
        {
            SmtpServer = "smtp.mailtrap.io";
            Port = 2525;
            UseSSL = false;
            SmtpUsername = Environment.GetEnvironmentVariable("MailtrapSmtpUsername");
            SmtpPassword = Environment.GetEnvironmentVariable("MailtrapSmtpPassword");
            SenderAddress = new MailboxAddress("Eucyon Bookit", "eucyon-bookit@hotmail.com");
        }
    }
}