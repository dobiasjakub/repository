using EucyonBookIt.Services.Abstracts;
using MimeKit;

namespace EucyonBookIt.Services
{
    public class EmailServiceHotmail : EmailServiceBase
    {
        public EmailServiceHotmail()
        {
            SmtpServer = "smtp.office365.com";
            Port = 587;
            UseSSL = false;
            SmtpUsername = Environment.GetEnvironmentVariable("HotmailSmtpUsername");
            SmtpPassword = Environment.GetEnvironmentVariable("HotmailSmtpPassword");
            SenderAddress = new MailboxAddress("Eucyon Bookit", "eucyon-bookit@hotmail.com");
        }
    }
}
