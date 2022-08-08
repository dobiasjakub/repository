using EucyonBookIt.Models;
using Microsoft.EntityFrameworkCore;

namespace EucyonBookIt.Database
{
    public static class DatabaseInitializer
    {
        public static void Seed(this ModelBuilder modelBuilder)
        {
            Role userRole = new Role() { RoleId = 1, RoleName = "Customer", Users = new List<User> { } };
            Role managerRole = new Role() { RoleId = 2, RoleName = "Manager", Users = new List<User> { } };
            Role adminRole = new Role() { RoleId = 3, RoleName = "Admin", Users = new List<User> { } };

            Person userPerson = new Person() { PersonId = 1, FirstName = "Jenda", LastName = "Smith", Occupancies = new List<Occupancy> { }, UserId = 1 };
            Person managerPerson = new Person() { PersonId = 2, FirstName = "Mr", LastName = "Manage", Occupancies = null, UserId = 2 };
            Person adminPerson = new Person() { PersonId = 3, FirstName = "Leo", LastName = "DiCaprio", Occupancies = null, UserId = 3 };

            User user = new User() { UserId = 1, EmailAddress = "my-address@koteb.nt", Password = "th3reMustAlw4ysBeAPassword", RoleId = 1, IsActive = true, ManagedHotels = null, PersonId = 1, Reservations = new List<Reservation> { } };
            User manager = new User() { UserId = 2, EmailAddress = "MrManage@holidaywin.com", Password = "Password123", RoleId = 2, IsActive = true, Reservations = null, PersonId = 2, ManagedHotels = new List<Hotel> { } };
            User admin = new User() { UserId = 3, EmailAddress = "LeoD@titanic.com", Password = "TitanicMustSink2Bottom", RoleId = 3, IsActive = true, Reservations = null, PersonId = 3 };

            Reservation reservation = new Reservation() { ReservationId = 1, UserId = 1, Status = 0, ReviewId = 1, CreatedAt = DateTime.UtcNow, UpdatedAt = DateTime.UtcNow, Occupancies = new List<Occupancy> { } };
            Reservation reservation2 = new Reservation() { ReservationId = 2, UserId = 1, Status = 0, CreatedAt = DateTime.UtcNow, UpdatedAt = DateTime.UtcNow, Occupancies = new List<Occupancy> { } };
            Reservation reservation3 = new Reservation() { ReservationId = 3, UserId = 1, Status = 0, CreatedAt = DateTime.UtcNow, UpdatedAt = DateTime.UtcNow, Occupancies = new List<Occupancy> { } };
            
            Hotel hotel = new Hotel() { HotelId = 1, Name = "Dummy Hotel", Location = "In Memory", Description = "Just a good ol' fake", UserId = 2, Reviews = new List<Review> { }, Rooms = new List<Room> { } };
            Hotel dreamFactory = new Hotel() { HotelId = 2, Name = "Dream factory", Location = "In Memory", Description = "The best fake you´ll ever get", UserId = 2, Reviews = new List<Review> { }, Rooms = new List<Room> { } };
            Hotel istriana = new Hotel() { HotelId = 3, Name = "Istriana", Location = "In Memory", Description = "You want real? Not here...", UserId = 2, Reviews = new List<Review> { }, Rooms = new List<Room> { } };

            Room room = new Room() { RoomId = 1, Description = "Best memory location for your 1s and 0s", HotelId = 1, Capacity = 5, Price = 3000, IsActive = true, Occupancies = new List<Occupancy> { } };
            Room dreamRoom = new Room() { RoomId = 2, Description = "Best memory location for your 1s and 0s", HotelId = 2, Capacity = 4, Price = 2500, IsActive = true, Occupancies = new List<Occupancy> { } };
            Room rovinj = new Room() { RoomId = 3, Description = "Best memory location for your 1s and 0s", HotelId = 3, Capacity = 10, Price = 13000, IsActive = true, Occupancies = new List<Occupancy> { } };

            Occupancy occupancy = new Occupancy() { OccupancyId = 1, ReservationStart = DateTime.Now, ReservationEnd = DateTime.Now.AddDays(5), ReservationId = 1, RoomId = 1, Occupants = new List<Person> { } };
            Occupancy occupancy2 = new Occupancy() { OccupancyId = 2, ReservationStart = DateTime.Now.AddMonths(1), ReservationEnd = DateTime.Now.AddMonths(2), ReservationId = 2, RoomId = 2, Occupants = new List<Person> { } };
            Occupancy occupancy3 = new Occupancy() { OccupancyId = 3, ReservationStart = DateTime.Now.AddMonths(10), ReservationEnd = DateTime.Now.AddMonths(11), ReservationId = 3, RoomId = 3, Occupants = new List<Person> { } };

            Review review = new Review() { ReviewId = 1, Title = "Best stay in RAM", CreatedAt = DateTime.UtcNow, Stars = 5, ReservationId = 1, HotelId = 1, Text = "Loved the stay, recommend!" };

            modelBuilder.Entity<Hotel>().HasData(hotel);
            modelBuilder.Entity<Hotel>().HasData(dreamFactory);
            modelBuilder.Entity<Hotel>().HasData(istriana);

            modelBuilder.Entity<Log>().HasData(
                new Log { Id = 1, Message = "Seeded message", MessageTemplate = "", Level = "", TimeStamp = DateTime.Now, Exception = "", Properties = "" });

            modelBuilder.Entity<Occupancy>().HasData(occupancy);
            modelBuilder.Entity<Occupancy>().HasData(occupancy2);
            modelBuilder.Entity<Occupancy>().HasData(occupancy3);

            modelBuilder.Entity<Person>().HasData(userPerson, managerPerson, adminPerson);

            modelBuilder.Entity<Reservation>().HasData(reservation);
            modelBuilder.Entity<Reservation>().HasData(reservation2);
            modelBuilder.Entity<Reservation>().HasData(reservation3);

            modelBuilder.Entity<Review>().HasData(review);

            modelBuilder.Entity<Role>().HasData(userRole, managerRole, adminRole);

            modelBuilder.Entity<Room>().HasData(room);
            modelBuilder.Entity<Room>().HasData(dreamRoom);
            modelBuilder.Entity<Room>().HasData(rovinj);

            modelBuilder.Entity<User>().HasData(user, manager, admin);
        }
    }
}
