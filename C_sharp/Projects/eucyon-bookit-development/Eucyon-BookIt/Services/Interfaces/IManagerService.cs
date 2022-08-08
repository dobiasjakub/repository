using EucyonBookIt.Models.DTOs;

namespace EucyonBookIt.Services.Interfaces
{
    public interface IManagerService
    {
        List<RoomOccupancyDTO> GetHotelReservationsByManagerEmail(string email);
        List<HotelReservationsDTO> GetReservationsByHotelsByManagerEmail(string email);
    }
}
