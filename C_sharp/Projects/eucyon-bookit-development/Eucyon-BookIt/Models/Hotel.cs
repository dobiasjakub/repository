namespace EucyonBookIt.Models
{
    public class Hotel
    {
        public long HotelId { get; set; }
        public string Name { get; set; }
        public string Location { get; set; }
        public string Description { get; set; }
        public List<Room>? Rooms { get; set; }
        public long UserId { get; set; }
        public User User { get; set; } 
        public List<Review>? Reviews { get; set; }
    }
}
