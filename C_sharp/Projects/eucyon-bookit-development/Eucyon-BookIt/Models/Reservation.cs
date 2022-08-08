using System.ComponentModel.DataAnnotations;

namespace EucyonBookIt.Models
{
    public class Reservation
    {
        [Key]
        public long ReservationId { get; set; }
        public long UserId { get; set; }
        public User User { get; set; }
        public int Status { get; set; }
        public long? ReviewId { get; set; } = null;
        public Review? Review { get; set; } = null;
        public List<Occupancy> Occupancies { get; set; }
        public DateTime CreatedAt { get; set; } = DateTime.Now;
        public DateTime UpdatedAt { get; set; } = DateTime.Now;
    }
}
