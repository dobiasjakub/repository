namespace EucyonBookIt.Models.DTOs
{
    public class OccupancyDTO
    {
        public long OccupancyId { get; set; }
        public DateTime ReservationStart { get; set; }
        public DateTime ReservationEnd { get; set; }
        public long ReservationId { get; set; }
        public ReservationDetailDTO Reservation { get; set; }
        public long RoomId { get; set; }
        public List<UserGuestDTO> Occupants { get; set; }
    }
}
