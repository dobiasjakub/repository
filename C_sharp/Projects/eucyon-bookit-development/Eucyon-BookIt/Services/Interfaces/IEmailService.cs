using MimeKit;

namespace EucyonBookIt.Services.Interfaces
{
    public interface IEmailService
    {
        bool Send(string name, string address, string subject, string body);
        bool Send(MimeMessage message);
    }
}
