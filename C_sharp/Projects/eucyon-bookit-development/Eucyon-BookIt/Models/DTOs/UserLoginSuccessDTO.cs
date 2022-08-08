using System.Runtime.Serialization;

namespace EucyonBookIt.Models.DTOs
{
    public class UserLoginSuccessDTO : BaseResponseDTO
    {
        public string AuthToken { get; set; } = string.Empty;
        public string DateIssued { get; set; } = DateTime.Now.ToString();

        public UserLoginSuccessDTO(string message, string authToken) : base(message)
        {
            AuthToken = authToken;
        }
    }
}
