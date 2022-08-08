using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;

namespace EucyonBookIt.Services.Interfaces
{
    public interface IUserService
    {
        bool ValidateEmailAddress(string emailAddress, out string returnMessage);
        bool ValidatePassword(string password, string passwordRetype, out string returnMessage);
        User? RegisterUser(UserRegistrationDTO dto);
        User AddUser(User newUser, bool activate);
        User? GetUserBy(string username, bool includePerson = false);
        User? GetUserBy(string username, string password);
        User? LoginUser(UserLoginDTO dto);
        User? ResetPassword(ResetPasswordDTO dto);
        string GenerateNewPassword();
        List<User> GetUsers();
        User? GetUserBy(User? user);
        public bool UpdateUser(User user);
    }
}
