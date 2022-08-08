namespace EucyonBookIt.Models.DTOs
{
    public class HotelReviewDTO
    {
        public string Title { get; set; }
        public string Text { get; set; }
        public DateTime CreatedAt { get; set; } 
        public int Stars { get; set; }
        public long ReservationId { get; set; }
        public long HotelId { get; set; }
    }
}
