namespace EucyonBookIt.Models
{
    public class User
    {
        public long UserId { get; set; }
        public string EmailAddress { get; set; }
        public string Password { get; set; }
        public bool? IsActive { get; set; }
        public int? RoleId { get; set; }
        public Role? Role { get; set; }
        public List<Hotel>? ManagedHotels { get; set; } = null;
        public List<Reservation>? Reservations { get; set; } = null;
        public long? PersonId { get; set; }
        public Person? Person { get; set; }
    }
}
