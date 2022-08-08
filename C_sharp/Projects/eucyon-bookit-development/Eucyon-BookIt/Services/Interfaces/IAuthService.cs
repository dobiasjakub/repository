using EucyonBookIt.Models;

namespace EucyonBookIt.Services.Interfaces
{
    public interface IAuthService
    {
        string CreateToken(User user);
    }
}
