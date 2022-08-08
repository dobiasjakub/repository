using AutoMapper;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;

namespace EucyonBookIt.Profiles
{
    public class ReservationProfile : Profile
    {
        public ReservationProfile()
        {
            CreateMap<Reservation, ReservationDetailDTO>();
            CreateMap<Occupancy, OccupancyDTO>()
                .ForMember(dest => dest.Reservation, act => act.MapFrom(src => src.Reservation));
            CreateMap< User, UserGuestDTO>();
            CreateMap< Person, PersonGuestDTO>();
        }
    }
}
