using EucyonBookIt.Database;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;
using AutoMapper;
using Microsoft.EntityFrameworkCore;
using EucyonBookIt.Services.Interfaces;
using System.Collections.Generic;

namespace EucyonBookIt.Services
{
    public class HotelService : IHotelService
    {
        private readonly ApplicationContext _applicationContext;
        private readonly IMapper _mapper;

        public HotelService(ApplicationContext applicationContext, IMapper mapper)
        {
            this._applicationContext = applicationContext;
            this._mapper = mapper;
        }

        public HotelDetailsDTO GetHotelDetailsById(long id)
        {
            var hotel = _applicationContext.Hotels.Include(x => x.Rooms).Include(x => x.Reviews).Include(x => x.User).FirstOrDefault(x => x.HotelId == id);
            return _mapper.Map<HotelDetailsDTO>(hotel);
        }

        public HotelDetailsDTO GetHotelByLocationAndName(string location, string name)
        {
            var hotel = _applicationContext.Hotels.Include(x => x.Rooms).Include(x => x.Reviews).Include(x => x.User).FirstOrDefault(x => x.Name.ToLower() == name.ToLower() && x.Location.ToLower() == location.ToLower());
            return _mapper.Map<HotelDetailsDTO>(hotel);
        }

        public ListOfHotelDetailsDTO GetHotelsByLocation(string location)
        {
            List<Hotel> hotels = _applicationContext.Hotels.Include(x => x.Rooms).Include(x => x.Reviews).Include(x => x.User).Where(x => x.Location.ToLower() == location.ToLower()).OrderBy(h => h.Name).ToList();
            
            return new ListOfHotelDetailsDTO
            {
                Location = location,
                Hotels = _mapper.Map<List<HotelDetailsDTO>>(hotels)
            };
        }

        public ListOfHotelNamesDTO GetHotelNamesByLocation(string location)
        {
            List<Hotel> hotels = _applicationContext.Hotels.Include(x => x.Rooms).Include(x => x.Reviews).Include(x => x.User).Where(x => x.Location.ToLower() == location.ToLower()).OrderBy(h => h.Name).ToList();
            List<string> hotelNames = new List<string>();

            foreach (Hotel hotel in hotels)
            {
                string hotelName = hotel.Name;
                hotelNames.Add(hotelName);
            }

            return new ListOfHotelNamesDTO
            {
                Location = location,
                HotelNames = hotelNames
            };        
        }

       
    }
}
