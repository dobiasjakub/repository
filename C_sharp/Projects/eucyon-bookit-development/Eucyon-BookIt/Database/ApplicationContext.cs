using EucyonBookIt.Models;
using Microsoft.EntityFrameworkCore;

namespace EucyonBookIt.Database
{
    public class ApplicationContext : DbContext
    {
        public virtual DbSet<Hotel> Hotels { get; set; }
        public virtual DbSet<User> Users { get; set; }
        public virtual DbSet<Room> Rooms { get; set; }
        public virtual DbSet<Review> Reviews { get; set; }
        public virtual DbSet<Reservation> Reservations { get; set; }
        public virtual DbSet<Person> People { get; set; }
        public virtual DbSet<Role> Roles { get; set; }
        public virtual DbSet<Log> Logs { get; set; }

        public ApplicationContext()
        {
        }

        public ApplicationContext(DbContextOptions options) : base(options) { }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.EnableSensitiveDataLogging();
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // FLUENT API
            // Properties of User table
            modelBuilder.Entity<User>().HasKey(u => u.UserId);
            modelBuilder.Entity<User>().Property(u => u.EmailAddress).HasColumnType("varchar(60)").IsRequired();
            modelBuilder.Entity<User>().Property(u => u.Password).HasColumnType("varchar(30)").IsRequired();
            modelBuilder.Entity<User>().Property(u => u.IsActive).HasColumnType("bit").IsRequired();
            modelBuilder.Entity<User>().HasOne(u => u.Role).WithMany(r => r.Users).HasForeignKey(u => u.RoleId).OnDelete(DeleteBehavior.NoAction);
            modelBuilder.Entity<User>().HasOne(u => u.Person).WithOne(p => p.User).HasForeignKey<Person>(u => u.PersonId).OnDelete(DeleteBehavior.NoAction);

            // Properties of Room table
            modelBuilder.Entity<Room>().HasKey(u => u.RoomId);
            modelBuilder.Entity<Room>().HasOne(h => h.Hotel).WithMany(r => r.Rooms).HasForeignKey(r => r.HotelId);

            // Properties of Reservation table
            modelBuilder.Entity<Reservation>().HasKey(u => u.ReservationId);
            modelBuilder.Entity<Reservation>().HasOne(r => r.User).WithMany(r => r.Reservations); //will by updated by type of user
            modelBuilder.Entity<Reservation>().HasOne(r => r.Review).WithOne(r => r.Reservation).HasForeignKey<Review>(r => r.ReviewId).OnDelete(DeleteBehavior.NoAction);

            // Properties of Occupancy table
            modelBuilder.Entity<Occupancy>().HasKey(o => o.OccupancyId);
            modelBuilder.Entity<Occupancy>().HasOne(o => o.Reservation).WithMany(r => r.Occupancies).HasForeignKey(o => o.ReservationId).OnDelete(DeleteBehavior.NoAction);
            modelBuilder.Entity<Occupancy>().HasOne(o => o.Room).WithMany(r => r.Occupancies).HasForeignKey(o => o.OccupancyId).OnDelete(DeleteBehavior.NoAction);
            modelBuilder.Entity<Occupancy>().HasMany(o => o.Occupants).WithMany(p => p.Occupancies);

            // Properties of Review table
            modelBuilder.Entity<Review>().HasKey(r => r.ReviewId);
            modelBuilder.Entity<Review>().Property(r => r.Title).HasColumnType("varchar(50)").IsRequired();
            modelBuilder.Entity<Review>().Property(r => r.Text).IsRequired();
            modelBuilder.Entity<Review>().Property(r => r.Stars).IsRequired();
            modelBuilder.Entity<Review>().HasOne(r => r.Reservation).WithOne(r => r.Review).OnDelete(DeleteBehavior.NoAction);

            //properties of Hotel model
            modelBuilder.Entity<Hotel>().HasMany(h => h.Rooms).WithOne(h => h.Hotel);
            modelBuilder.Entity<Hotel>().HasOne(h => h.User).WithMany(h => h.ManagedHotels);
            modelBuilder.Entity<Hotel>().HasMany(h => h.Reviews).WithOne(h => h.Hotel).OnDelete(DeleteBehavior.NoAction);

            //Properties of Person table
            modelBuilder.Entity<Person>().HasKey(p => p.PersonId);
            modelBuilder.Entity<Person>().Property(p => p.FirstName).HasColumnType("varchar(40)").IsRequired();
            modelBuilder.Entity<Person>().Property(p => p.LastName).HasColumnType("varchar(40)").IsRequired();

            //Properties of Role table
            modelBuilder.Entity<Role>().HasKey(r => r.RoleId);
            modelBuilder.Entity<Role>().Property(r => r.RoleName).HasColumnType("varchar(40)").IsRequired();

            //Properties of Log model
            modelBuilder.Entity<Log>().HasKey(x => x.Id);

            //Data seeding
            DatabaseInitializer.Seed(modelBuilder);
        }
    }
}


