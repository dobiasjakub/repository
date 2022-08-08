using System.ComponentModel.DataAnnotations;

namespace EucyonBookIt.Models
{
    public class Room
    {
        [Key]
        public long RoomId { get; set; }
        public string Description { get; set; }
        public long HotelId { get; set; }
        public Hotel Hotel { get; set; }
        public int Capacity { get; set; }
        public List<Occupancy> Occupancies { get; set; }
        public double Price { get; set; }
        public bool IsActive { get; set; }
    }
}
