namespace EucyonBookIt.Models.DTOs
{
    public class HotelReservationsDTO
    {
        public long HotelId { get; set; }
        public List<RoomOccupancyDTO> Rooms{ get; set; }
    }
}
