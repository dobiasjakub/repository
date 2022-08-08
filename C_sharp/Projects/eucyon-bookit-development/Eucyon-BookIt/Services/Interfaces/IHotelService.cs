using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;

namespace EucyonBookIt.Services.Interfaces
{
    public interface IHotelService
    {
        HotelDetailsDTO GetHotelByLocationAndName(string location,string name);
        HotelDetailsDTO GetHotelDetailsById(long id);
        ListOfHotelDetailsDTO GetHotelsByLocation(string location);
        ListOfHotelNamesDTO GetHotelNamesByLocation(string location);
    }
}
