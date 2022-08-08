using System.ComponentModel.DataAnnotations.Schema;

namespace EucyonBookIt.Models
{
    public class Review
    {
        public long ReviewId { get; set; }
        public string Title { get; set; }
        public string Text { get; set; }
        public DateTime CreatedAt { get; set; } = DateTime.Now;
        public int Stars { get; set; }
        public long ReservationId { get; set; }
        public Reservation Reservation { get; set; }

        [ForeignKey("ReviewForeignKey")]
        public long HotelId { get; set; }
        public Hotel Hotel { get; set; }
    }
}
