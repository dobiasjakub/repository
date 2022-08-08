using EucyonBookIt.Services;

namespace EucyonBookIt.Models.DTOs
{
    public class HotelDetailsDTO
    {
        public long HotelId { get; set; }
        public string Name { get; set; }
        public string Location { get; set; }
        public string Description { get; set; }
        public List<RoomDetailsDTO>? Rooms { get; set; }  
        public List<HotelReviewDTO>? Reviews { get; set; }
        public UserManagerDTO Manager { get; set; }
    }  
}
