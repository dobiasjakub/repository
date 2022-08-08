using EucyonBookIt.Models.Exceptions;
using EucyonBookIt.Services.Interfaces;
using MailKit.Net.Smtp;
using MimeKit;

namespace EucyonBookIt.Services.Abstracts
{
    public abstract class EmailServiceBase : IEmailService
    {  
        protected string SmtpServer { get; set; }
        protected int Port { get; set; }
        protected bool UseSSL { get; set; }
        protected string SmtpUsername { get; set; }
        protected string SmtpPassword { get; set; }
        protected MailboxAddress SenderAddress { get; set; }

        public virtual bool Send(string name, string address, string subject, string body)
        {
            string response;
            var message = BuildMessage(name, address, subject, body);

            try
            {
                response = SendMail(message);
            }
            catch (Exception e)
            {
                throw new EmailServiceException("There was an error in communication with a mailing server. Your request was aborted.");
            }

            return ValidateResponse(response);
        }

        public virtual bool Send(MimeMessage message)
        {
            string response;
            message = BuildMessage(message);
            
            try
            {
                response = SendMail(message);
            }
            catch (Exception e)
            {
                throw new EmailServiceException("There was an error in communication with a mailing server. Your request was aborted.");
            }

            return ValidateResponse(response);
        }

        protected MimeMessage BuildMessage(string name, string address, string subject, string body)
        {
            var message = new MimeMessage();
            message.From.Add(SenderAddress);
            message.To.Add(new MailboxAddress(name, address));
            message.Subject = subject;
            message.Body = new TextPart(MimeKit.Text.TextFormat.Text)
            {
                Text = body
            };

            return message;
        }

        protected MimeMessage BuildMessage(MimeMessage message)
        {
            if (message.From.Count == 0)
                message.From.Add(SenderAddress);

            return message;
        }

        protected virtual string SendMail(MimeMessage message)
        {
            string response;

            using (var client = new SmtpClient())
            {
                client.ServerCertificateValidationCallback = (s, c, h, e) => true;

                client.Connect(SmtpServer, Port, UseSSL);
                client.Authenticate(SmtpUsername, SmtpPassword);
                response = client.Send(message);
                client.Disconnect(true);
            }

            return response;
        }

        protected virtual bool ValidateResponse(string response)
        {
            string expectedOk = "2.0.0 ok";

            if (response.ToLower().StartsWith(expectedOk))
                return true;
            else
                return false;
        }
    }
}
