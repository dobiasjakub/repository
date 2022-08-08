namespace EucyonBookIt.Models
{
    public class Occupancy
    {
        public long OccupancyId { get; set; }
        public DateTime ReservationStart { get; set; }
        public DateTime ReservationEnd { get; set; }
        public long ReservationId { get; set; }
        public Reservation Reservation { get; set; }
        public long RoomId { get; set; }
        public Room Room { get; set; }
        public List<Person> Occupants { get; set; }
    }
}
