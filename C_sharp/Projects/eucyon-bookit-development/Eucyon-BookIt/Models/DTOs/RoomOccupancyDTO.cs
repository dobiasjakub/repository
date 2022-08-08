namespace EucyonBookIt.Models.DTOs
{
    public class RoomOccupancyDTO
    {
        public int HotelId { get; set; }
        public int RoomId { get; set; }
        public List<OccupancyDTO> Occupancies { get; set; }
    }
}
