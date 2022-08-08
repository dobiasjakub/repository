using AutoMapper;
using EucyonBookIt.Database;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;
using EucyonBookIt.Services.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace EucyonBookIt.Services
{
    public class ManagerService : IManagerService
    {
        private readonly ApplicationContext _applicationContext;
        private readonly IMapper _mapper;

        public ManagerService(ApplicationContext applicationContext, IMapper mapper)
        {
            _applicationContext = applicationContext;
            _mapper = mapper;
        }

        public List<RoomOccupancyDTO> GetHotelReservationsByManagerEmail(string emailAddress)
        {
            var manager = _applicationContext.Users.FirstOrDefault(x => x.EmailAddress == emailAddress);
            List<Room> rooms = _applicationContext.Rooms.Include(x => x.Occupancies.Where(x => x.ReservationStart > DateTime.Now).OrderBy(x=>x.ReservationStart)).ThenInclude(x => x.Reservation).ThenInclude(x => x.User).ThenInclude(x => x.Person).Where(x => x.Hotel.UserId == manager.UserId).ToList();
            List<RoomOccupancyDTO> occupancies = _mapper.Map<List<RoomOccupancyDTO>>(rooms);
            return occupancies;
        }

        public List<HotelReservationsDTO> GetReservationsByHotelsByManagerEmail(string emailAddress)
        {
            var manager = _applicationContext.Users.FirstOrDefault(x => x.EmailAddress == emailAddress);
            List<Hotel> hotels = _applicationContext.Hotels.Where(h => h.UserId == manager.UserId).Include(x => x.Rooms).ThenInclude(x => x.Occupancies.Where(x => x.ReservationStart > DateTime.Now).OrderBy(x => x.ReservationStart)).ThenInclude(x => x.Reservation).ThenInclude(x => x.User).ThenInclude(x => x.Person).ToList();
            List<HotelReservationsDTO> reservations = _mapper.Map<List<HotelReservationsDTO>>(hotels);
            return reservations;
        }
    }
}
