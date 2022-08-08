using AutoMapper;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;

namespace EucyonBookIt.Profiles
{
    public class HotelProfiles : Profile
    {
        public HotelProfiles()
        {
            CreateMap<Hotel, HotelDetailsDTO>()
                .ForMember(dest => dest.Rooms, act => act.MapFrom(src => src.Rooms))
                .ForMember(dest => dest.Reviews, act => act.MapFrom(src => src.Reviews))
                .ForMember(dest => dest.Manager, act => act.MapFrom(src => src.User));
            CreateMap<Review, HotelReviewDTO>();
            CreateMap<Room, RoomDetailsDTO>();
            CreateMap<User, UserManagerDTO>();
            CreateMap<Room, RoomOccupancyDTO>();
            CreateMap<Hotel, HotelReservationsDTO>();
        }    
    }
}
