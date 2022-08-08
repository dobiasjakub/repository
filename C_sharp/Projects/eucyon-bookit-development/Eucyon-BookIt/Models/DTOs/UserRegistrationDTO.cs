using EucyonBookIt.Models;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace EucyonBookIt.Models.DTOs
{
    public class UserRegistrationDTO
    {
        public string EmailAddress { get; set; }
        public string Password { get; set; }
        public string PasswordRetype { get; set; }
        public string Role { get; set; }
    }
}
