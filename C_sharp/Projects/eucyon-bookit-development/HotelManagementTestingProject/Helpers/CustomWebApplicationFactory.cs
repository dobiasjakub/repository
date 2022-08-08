using EucyonBookIt.Database;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.Data.Sqlite;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.DependencyInjection.Extensions;
using Microsoft.Extensions.Hosting;

namespace HotelManagementTestingProject.Helpers
{
    public class CustomWebApplicationFactory : WebApplicationFactory<Program>
    {
        private SqliteConnection _connection;

        protected override IHost CreateHost(IHostBuilder builder)
        {
            var _connection = new SqliteConnection("Datasource=:memory:");
            _connection.Open();

            builder.ConfigureServices(services =>
            {
                services.RemoveAll(typeof(DbContextOptions<ApplicationContext>));
                services.AddDbContext<ApplicationContext>(options =>
                {
                    options.UseSqlite(_connection);
                });
            });

            return base.CreateHost(builder);
        }

        protected override void Dispose(bool disposing)
        {
            //not needed?
            //_connection.Close();
            base.Dispose(disposing);
        }
    }
}
