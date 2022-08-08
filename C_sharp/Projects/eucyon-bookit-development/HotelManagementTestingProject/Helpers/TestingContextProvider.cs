using EucyonBookIt.Database;
using EucyonBookIt.Models;
using Microsoft.Data.Sqlite;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Moq;

namespace HotelManagementTestingProject.Helpers
{
    public static class TestingContextProvider
    {
        public static ApplicationContext CreateContextFromScratch()
        {
            var dbConnection = new SqliteConnection("Datasource=:memory:");
            dbConnection.Open();
            var contextOptions = new DbContextOptionsBuilder<ApplicationContext>().UseSqlite(dbConnection).Options;
            var context = new ApplicationContext(contextOptions);

            context.Database.EnsureCreated();

            return context;
        }

        public static ApplicationContext CreateContextFromFactory(CustomWebApplicationFactory factory)
        {
            var scope = factory.Services.CreateScope();
            var context = scope.ServiceProvider.GetRequiredService<ApplicationContext>();

            var connection = context.Database.GetDbConnection();
            connection.Close();
            connection.Open();

            context.Database.EnsureDeleted();
            context.Database.EnsureCreated();

            return context;
        }

        public static Mock<ApplicationContext> CreateEmptyMockContext()
        {
            return new Mock<ApplicationContext>();
        }
    }
}
