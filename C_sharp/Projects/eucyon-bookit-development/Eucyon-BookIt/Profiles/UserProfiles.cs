using AutoMapper;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;

namespace EucyonBookIt.Profiles
{
    public class UserProfiles : Profile
    {
        public UserProfiles()
        {
            CreateMap<User, UserEditDTO>().ReverseMap();
            CreateMap<UserRegistrationDTO, User>().ForMember(source => source.Role, dest => dest.Ignore());
            CreateMap<UserLoginDTO, User>();
        }
    }
}