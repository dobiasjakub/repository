namespace EucyonBookIt.Models.DTOs
{
    public class RoomDetailsDTO
    {
        public long RoomId { get; set; }
        public long HotelId { get; set; }
        public string Description { get; set; }
        public int Capacity { get; set; }
        public double Price { get; set; }
        public bool IsActive { get; set; }
    }
}
