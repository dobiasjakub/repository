namespace EucyonBookIt.Models.DTOs
{
    public class ReservationDetailDTO
    {
        public long ReservationId { get; set; }
        public UserGuestDTO User { get; set; }
        public int Status { get; set; }
        public DateTime CreatedAt { get; set; } = DateTime.Now;
        public DateTime UpdatedAt { get; set; } = DateTime.Now;
    }
}
