using MimeKit;

namespace EucyonBookIt.Models.MailModels
{
    public class MimePasswordReset : MimeMessage
    {
        public MimePasswordReset(string toName, string toEmailAddress, string newPassword)
        {
            To.Add(new MailboxAddress(toName, toEmailAddress));
            Subject = "Your new password";
            Body = new TextPart(MimeKit.Text.TextFormat.Text)
            {
                Text = "There was a request to reset password to your account. You can now sign in by using password: " + newPassword
            };
        }
    }
}
