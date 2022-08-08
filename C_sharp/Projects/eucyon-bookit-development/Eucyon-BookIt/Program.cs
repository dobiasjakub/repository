using EucyonBookIt.Database;
using EucyonBookIt.Middlewares;
using EucyonBookIt.Services;
using EucyonBookIt.Services.Interfaces;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Localization;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Serilog;
using Serilog.Events;
using Serilog.Sinks.MSSqlServer;
using System.Text;
using System.Globalization;
using System.Reflection;
using EucyonBookIt.Swagger;
using Microsoft.AspNetCore.Mvc.NewtonsoftJson;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
ConfigureSerilog(builder);

// Add .AddNewtonsoftJson() to available JsonPatch
builder.Services.AddControllers()
// Avoid infinite loop during serialization
                .AddNewtonsoftJson(x => x.SerializerSettings.ReferenceLoopHandling
                                     = Newtonsoft.Json.ReferenceLoopHandling.Ignore);
// Add Swagger Service
ConfigureSwagger(builder);

// Add AutoMapper Service
builder.Services.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

MapSecretsToEnvVariables();

ConfigureAuthentication(builder.Services);
ConfigureLocalization(builder.Services);
ConfigureDb(builder.Services);
ConfigureServices(builder.Services);

var app = builder.Build();

app.UseMiddleware<ExceptionHandlerMiddleware>();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseRequestLocalization(app.Services.GetService<IOptions<RequestLocalizationOptions>>().Value);
app.UseHttpLogging();
app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();

app.UseStaticFiles();
app.MapControllers();

app.Run();

static void ConfigureDb(IServiceCollection services)
{
    var config = services.BuildServiceProvider().GetRequiredService<IConfiguration>();
    var connectionString = config.GetConnectionString("DefaultConnection");
    services.AddDbContext<ApplicationContext>(b => b.UseSqlServer(connectionString));
}

static void ConfigureSerilog(WebApplicationBuilder? builder)
{
    var config = new ConfigurationBuilder()
        .AddJsonFile("appsettings.json", optional: false)
        .Build();

    var logger = new LoggerConfiguration()
        .MinimumLevel.Override("Microsoft", LogEventLevel.Information)
        .ReadFrom.Configuration(builder.Configuration)
        .Enrich.FromLogContext()
        // THIS PART WILL ENABLE ADDING LOGS TO THE DATABASE!
        //.WriteTo.MSSqlServer(
        //    connectionString: config.GetConnectionString("DefaultConnection"),
        //    sinkOptions: new MSSqlServerSinkOptions { TableName = "Logs", SchemaName = "dbo", AutoCreateSqlTable = true }
        //    )
        .CreateLogger();

    builder.Logging.ClearProviders();
    builder.Logging.AddSerilog(logger);
}

static void ConfigureServices(IServiceCollection services)
{
    services.AddSingleton<ExceptionHandlerMiddleware>();
    services.AddScoped<IHotelService, HotelService>();
    services.AddScoped<ApplicationContext>();
    services.AddScoped<IUserService, UserService>();
    services.AddScoped<IManagerService, ManagerService>();
    services.AddScoped<IAuthService, AuthService>();

#if DEBUG
    services.AddScoped<IEmailService, EmailServiceMailtrap>();
#elif RELEASE
        services.AddScoped<IEmailService, EmailServiceHotmail>();
#endif
}

static void ConfigureAuthentication(IServiceCollection services)
{
    services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme).AddJwtBearer(options =>
    {
        options.SaveToken = true;
        options.TokenValidationParameters = new TokenValidationParameters()
        {
            ValidateAudience = false,
            ValidateIssuer = false,
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Environment.GetEnvironmentVariable("JwtSigningKey"))),
        };
    });
}

static void ConfigureSwagger(WebApplicationBuilder? builder)
{
    builder.Services.AddSwaggerGen(options =>
    {
        options.SwaggerDoc("v1", new OpenApiInfo
        {
            Version = "v1",
            Title = "Eucyon BookIt API",
            Description = "An ASP.NET Core Web API for managing BookIt items"
        });

        options.OperationFilter<AcceptLanguageFilter>();

        // To Enable authorization using Swagger (JWT)  
        options.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme()
        {
            Name = "Authorization",
            Type = SecuritySchemeType.Http,
            Scheme = "Bearer",
            BearerFormat = "JWT",
            In = ParameterLocation.Header,
            Description = "JWT Authorization header using the Bearer scheme.",
        });

        options.AddSecurityRequirement(new OpenApiSecurityRequirement
                {
                    {
                          new OpenApiSecurityScheme
                            {
                                Reference = new OpenApiReference
                                {
                                    Type = ReferenceType.SecurityScheme,
                                    Id = "Bearer"
                                }
                            },
                            new string[] {}
                    }
                });

        var xmlFilename = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
        options.IncludeXmlComments(Path.Combine(AppContext.BaseDirectory, xmlFilename));
    }
    );
}

static void ConfigureLocalization(IServiceCollection services)
{
    services.AddLocalization();

    services.Configure<RequestLocalizationOptions>(
        options =>
        {
            var supportedCultures = new CultureInfo[] 
            { 
                new CultureInfo("en"),
                new CultureInfo("cs")
            };

            options.DefaultRequestCulture = new RequestCulture(culture: "en", uiCulture: "en");
            options.SupportedCultures = supportedCultures;
            options.SupportedUICultures = supportedCultures;
            options.ApplyCurrentCultureToResponseHeaders = true;
        });
}

static void MapSecretsToEnvVariables()
{
    var config = new ConfigurationBuilder().AddUserSecrets<Program>().Build();
    foreach (var child in config.GetChildren())
    {
        Environment.SetEnvironmentVariable(child.Key, child.Value);
    }
}

public partial class Program { }
