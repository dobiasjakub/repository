namespace EucyonBookIt.Models
{
    public class Person
    {
        public long PersonId { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public List<Occupancy>? Occupancies { get; set; } = null;
        public long? UserId { get; set; } = null;
        public User? User { get; set; } = null;
    }
}
