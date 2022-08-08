# Book It

[![dotnet package](https://github.com/green-fox-academy/eucyon-bookit/actions/workflows/build_test.yml/badge.svg)](https://github.com/green-fox-academy/eucyon-bookit/actions/workflows/build_test.yml)

The goal of the project is to create a system for booking hotel rooms and managing reservations. It is a backend focused ASP.NET web application.

- [Book It](#book-it)
  * [Links](#links)
  * [Features](#features)
  * [Tech features](#tech-features)
    + [Booking listings](#booking-listings)
      - [Get hotel details](#get-hotel-details)
      - [Get List of hotels for specific location](#get-list-of-hotels-for-specific-location)
  * [Authentication](#authentication)
  * [Authorization](#authorization)
    + [Roles](#roles)
      - [Customer](#customer)
      - [Manager](#manager)
      - [Admin](#admin)
    + [Access Grant to Controller/Endpoint](#access-grant-to-controller-endpoint)
    + [Testing](#testing)
  * [AutoMapper](#automapper)
    + [Documentation](#documentation)
    + [NuGet Package](#nuget-package)
    + [How To](#how-to)
      - [program.cs](#programcs)
      - [Profiles](#profiles)
      - [Dependency Injection (DI)](#dependency-injection--di-)
      - [Mapping](#mapping)
        * [Basic mapping](#basic-mapping)
        * [Update of existing object](#update-of-existing-object)
        * [Lists/Arrays](#lists-arrays)
        * [Advanced mapping](#advanced-mapping)
        * [Flattening](#flattening)
        * [Get methods](#get-methods)
      - [Testing](#testing)
      - [Tips & Tricks](#tips---tricks)
  * [Edit User](#edit-user)
    + [HTTP PUT](#http-put)
    + [HTTP PATCH](#http-patch)
      - [JsonPatch](#jsonpatch)
        * [Implementation and usage](#implementation-and-usage)
        * [Remarks](#remarks)
  * [E-mail sending](#e-mail-sending)
    + [NuGet Package](#nuget-package-1)
    + [Service structure](#service-structure)
    + [How To](#how-to-1)
      - [Depedency Injection](#depedency-injection)
      - [Using EmailService](#using-emailservice)
      - [Testing](#testing-1)
    + [Exception handling](#exception-handling)
    + [Unit and integration testing](#unit-and-integration-testing)
      - [Endpoint testing](#endpoint-testing)
      - [Dependency injection](#dependency-injection)
    + [Swagger UI](#swagger-ui)
      - [Usage](#usage)
      - [XML Comment](#xml-comment)
      - [Attribute Annotations](#attribute-annotations)
      - [Additional information](#additional-information)

## Links


## Features

- [Booking listings](#booking-listings)
- [User roles and authentication](#user-roles-and-authentication)
- Reservation management
- User reviews
- Email notifications
- Image handling

## Tech features

- Persistency
- Web API
- [Swagger UI](#swagger-ui)
- CI/CD Pipeline
- Hosting
- Logging
- [AutoMapper](#automapper)
- [Localization](#localization)
- [E-mail sending](#e-mail-sending)
- [Exception handling](#exception-handling)
- [Unit and integration testing](#unit-and-integration-testing)

### Booking listings
#### Get hotel details 
Customer can get all possible details about hotel using two different endpoints:

- 'api/hotel/id/{id}', where {id} means specific id of the hotel, (id is a number). 

- 'api/hotel/locationAndName/{location}/{name}', where {location} means specific location setted for hotel, for example 'Prague', and name means specific name setted for hotel, for example 'Continental'

All the propperties are not case sensitive, but if there are more words in some, like location = 'Buenos Aires', there must be a space between words.

Each endpoint returns JSON file with all possible details of selected hotel. If hotel is not found, code 404 and error message "Hotel is not found" returned.

#### Get List of hotels for specific location
Customer can see all hotels, which exists in specified location. There are two possible ways of view 
- List of Hotel names in specified location
- List of Hotels with all details included

Endpoints: 
- 'api/hotel/hotels/location/{location}/names' -> where {location} means specific location setted for hotel. This endpoint returns JSON file with list of HotelNames specific for setted location.

- 'api/hotel/hotels/location/{location}/details' -> where {location} means specific location setted for hotel. This endpoint returns JSON file with List of Hotels (all details of each hotel included) specific for setted location.

All the propperties are not case sensitive, but if there are more words in some, like location = 'Buenos Aires', there must be a space between words.

If there is no hotel in specified location, each endpoint returns code 404 and error message "There is no hotel in dedicated location."

#### Get upcoming reservations for Hotels (Manager Role)
Hotel manager can get all upcoming reservations for his hotels. There are two possible ways of view
- list of reservations for each managed hotel sorted by date of Reservation start. All possible details about reservation included
Format : Hotel -> Reservation 1, Reservation 2, ... 
- list of reservations, for each managed room in each hotel. If there are more reservations for one room, they are sorted by Start of reservation
Format : Hotel -> Room 1 -> Reseravtion 1, Reservation 2, ...

Hotel manager must be logged in application , requested userRole=Manager. For getting reservations, endpoint using Managers email getted from JWT token, and in response all reservations related to managers hotels are sent.¨

Endpoints:
- 'api/manager/reservations' -> . This endpoint returns JSON file with list of upcoming reservations for each hotel managed by hotel manager. If there is no reservation for hotel, it is shown as empty occupancies list in DTO.

- 'api/manager/reservations/hotels' -> This endpoint returns JSON file with all hotels/rooms managed by hotel manager. Under each room, the upcoming reservations is shown.

If endpoints are reached by user with lower level of authorization (for ex. 'Customer'), code 403 is returned in response.

If endpoints are reached with no authentification or authorization, code 401 is returned in response. 


## Authentication

We are using token-based authentication model with JWT as bearer token. [More info on JWT and token decoder](https://jwt.io/).

Token is created on successful login by `CreateToken()` method in `AuthService`. 

```
    public string CreateToken(User user)
    {
        var claims = new Claim[]
        {
            new Claim("EmailAddress", user.EmailAddress),
            new Claim("Role", user.Role.RoleName)
        };
        
        var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Environment.GetEnvironmentVariable("JwtSigningKey")));
        var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha512);
        var token = new JwtSecurityToken(
            claims: claims,
            expires: DateTime.Now.AddDays(1),
            signingCredentials: credentials);

        var jwt = new JwtSecurityTokenHandler().WriteToken(token);

        return jwt;
    }
```

Encryption algorithm used is HS512. Unique signing key which influences the final form of signature part of token and allows its verification is stored in user secrets (converted to EnvVariables because Github Actions workflow is unable to access it otherwise). Payload carries several claims (`UserId`, `EmailAddress`, `RoleName`) which are used for authorization, as well as database queries. Token expires 24 hours after its generation.

Tokens are validated by middleware configured in `Program.cs`. SaveToken option is true, so token is saved to `HttpContext` for use in endpoints.

```
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
```

## Authorization

### Roles

It is used Role-based authorization.
We created three roles
* Customer
* Manager
* Admin

#### Customer
Can login, edit own bookings and user details.

#### Manager
Can login, edit own bookings (if exist), user details, but also manage own hotels.

#### Admin
Can do things as Manager, plus change Users' roles and edit others information (User, Hotel etc.).

Information about Role is stored in DB `User.Role.RoleName` and it retrieved when JWT token is created.

Received information about user (username and role) can be received as property for UserName `HttpContext.User.Identity.Name` or for Role as method `HttpContext.User.IsInRole("Admin")`.

### Access Grant to Controller/Endpoint
In our project, it can be set Authorization in three ways for Controller or Endpoint.

1. `[Authorize]` - grant access to anyone with correct JWT token either to endpoint, or to controller and its all endpoints.
2. `[AllowAnonymous]` - allows access to non-authorized user's to specificic endpoint, if `[Authorize]` for controller is used, e.g. `/login`.
3. `[Authorize(Roles = "Admin")]` - specifies Role that has access to the endpoint.
    * More generous case is `[Authorize(Roles = "Manager, Admin")]` that grants access for `Manager` or `Admin` role.

### Testing
It is neccessary to Authorization for Authorized endpoint for each testcase.

User helper static method `GetTestingJWTToken.GetToken(string userName, string role)` to obtain JWT token.

It is used in HTTP Client as follows:

```
var httpClient = _factory.CreateClient();
httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt);
```

As *Act* user `var response = await httpClient.GetAsync`.

For testing, it advisable to test statuses
- 401 - Unauthorized
- 403 - Forbidden

[More information](https://docs.microsoft.com/en-us/aspnet/core/security/authorization/roles?view=aspnetcore-6.0)

## AutoMapper
*For more information contact Tonda*

`using AutoMapper;`

### Documentation
[AutoMapper Documentation](https://docs.automapper.org)
[Introduction to AutoMapper in ASP.NET Core 6](https://www.youtube.com/watch?v=lUGZrrx6fHI)

### NuGet Package
Install NuGet Package **AutoMapper.Extensions.Microsoft.Dependency** (already included)

### How To
#### program.cs
Register AutoMapper to Service Container. Parameter is `Assembly`, to abtain it Reflection is used.
`builder.Services.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());`
#### Profiles
New `Profiles` folder is created for AutoMapper profiles. Files describing AutoMapper behaviour for mapped pairs.
It implements interface `Profile` from AutoMapper.
`public class UserProfiles : Profile`
Basic syntax for mapping is
*source* -> *destination*
`CreateMap<User, UserDTO>();`

#### Dependency Injection (DI)
DI to Controller is done as usual
`private readonly IMapper _mapper;`

```
public Controller(IMapper mapper)
    {
        _mapper = mapper;
    }
```

#### Mapping
##### Basic mapping
In Controller use mapper as follows (not implemented yet):
`var myUserDTO = _mapper.Map<UserDTO>(user);`
- where `user` is instance of `User`: `var user = new User()`

Result is mapped UserDTO. It is expexcted that properties have same name in source as well as in destination. Otherwise, properties will not be mapped and require more detailed configuration.

##### Update of existing object
In case you need update existing object, use mapper in this way:
```
user = _mapper.Map<UserDTO, User>(userDTO, user)
```
- `User user` (containing some data, to be updated) - destination
  `UserDTO userDTO` (object with actual data) - source
  
Result is `user` with updated properties, other are not touched. 

##### Lists/Arrays
AutoMapper supports mapping of lists and arrays too:
`var usersDTO = _mapper.Map<List<UserDTO>>(users);`
- where `users` is list of `user` instances of type `User`: `var users = new List<User>()`
[More information](https://docs.automapper.org/en/stable/Lists-and-arrays.html)

##### Advanced mapping
In case that some property does not match between source and destination classes, there is required additional configuration in Profiles.
This is just example:
```
CreateMap<User, UserDTO>()
        .ForMember(
            dest => dest.Last,
            opt => opt.MapFrom(src => $"{src.Firstname} {src.Lastname}"))
        .ForMember(
            ...
            )
        );
```
[More information](https://docs.automapper.org/en/stable/Projection.html)

##### Flattening
For complex source objects that contain other object(s), it is advisable to use Flattening. It is process when complex object is mapped to the simple one. It uses syntax of **NameOfClass+NameOfProperty**, see example below.
Example with shortened classes.
`User.cs`
```
public class User
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public bool IsActive { get; set; }
        public long PersonId { get; set; }
        public Person Person { get; set; }
    }
```
`Person.cs`
```
public class Person
    {
        public long PersonId { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string EmailAddress { get; set; }
    }
```
Desired DTO with Person details `UserDTO.cs`
```
public class UserDTO
    {
        public string Username { get; set; }
        public long PersonId { get; set; }
        public long PersonPersonId { get; set; }
        public string PersonFirstName { get; set; }
        public string PersonLastName { get; set; }
    }
```
[More information](https://docs.automapper.org/en/stable/Flattening.html)
##### Get methods
It is possible to use method in source object with "Get" prefix as mapped property in the destination, e.g. GetTotal(). Then destination DTO will be:
```
public class UserDTO
    {
        public string Username { get; set; }
        public long PersonPersonId { get; set; }
        public decimal Total {get; set;}
    }
```

#### Testing
[Very basic information about Testing](https://docs.automapper.org/en/stable/Getting-started.html?highlight=testing#how-do-i-test-my-mappings)

#### Tips & Tricks
*To be updated*

## Edit User
### HTTP PUT
HTTP **PUT** method is generally used for updating full information. Here we user `UserPutDTO` containing following information:
```
public long? UserId { get; set; }
public string EmailAddress { get; set; }
public string Password { get; set; }
public string? NewPassword { get; set; }
public string PersonFirstName { get; set; }
public string PersonLastName { get; set; }
```
Only `Password`, `Person.FirstName` and `Person.LastName` can be edited.
Password can be changed if old and new password is provided.
Method directly store updates to DB.

### HTTP PATCH
HTTT **PATCH** method on the other hand updates only small part information, e.g. `Person.FirstName`.
This method uses [JsonPatch](https://jsonpatch.com/)

#### JsonPatch
JsonPatch implements HTTP Patch method using JSON document. With specific format of JSON document, it allow `add`, `remove`, `replace`, `copy`, `move` or `test` information.

##### Implementation and usage
* It is required to install the [Microsoft.AspNetCore.Mvc.NewtonsoftJson](https://www.nuget.org/packages/Microsoft.AspNetCore.Mvc.NewtonsoftJson/) NuGet package.
* In `program.cs` call `builder.Services.AddControllers()
    .AddNewtonsoftJson();` 
* Use `JsonPatchDocument<T>` in controller's endpoint, where T is patched type, e.g. `User`.
    ```
    public IActionResult PatchData(string email, [FromBody] JsonPatchDocument<User> patchUser)
    ```
* Patch is applied using `patchUser.ApplyTo(userFromDb, ModelState);`
* Patched `Used` is then stored to DB.

##### Remarks
* See more details in [API](API.md)
* MS documentation [JsonPatch in ASP.NET Core](https://docs.microsoft.com/en-us/aspnet/core/web-api/jsonpatch?view=aspnetcore-6.0)

## Localization
Application supports localized response messages. Supported languages are English and Czech, defined as `CultureInfo("en")` and `CultureInfo("cs")` in .NET. Default culture and UI culture is `en`. All responses include language of the response in header.

Localization middleware is configured in `Program.cs`:
```
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
```

Strings are stored in Resources directory, in .resx (StringResource.cs.resx etc.)files under `StringResource` class. These files contain every localized strings in sort of key-value pairs. Both must contain strings with same key for localization to work well. If string is not found (key doesn't exists in .resx file), application will return key instead of value.

To access localized string, `IStringsLocalizer<StringResource>` has to be injected to a class. Strings are then fetch like this:
```
_localizer["RegistrationFailure"]
```

Things to keep in mind:
- [] operator returns value of type LocalizedString if assigned to a non string variable or anonymous type property (to fetch a string, append `.Value`). For variables explicitly defined as string, localized string is automatically fetch as a string type.
- DateTime type has a predefined format that does not react to Culture change. To localize dates in responses, return DTO where dates are of type string.

Request language is based on Accept-Language header parameter in request. This option was added to Swagger UI by `AcceptLanguageFilter` that was added to Swagger configuration in `Program.cs`. Every request in Swagger UI now has a box allowing to input Accept-Language value.

## E-mail sending
*For more information contact Lukas"

Handled by MailKit and MimeKit libraries which make it possible to go through the complete email communication: creation of e-mails, SMTP server communication and authentication, and passing outgoing e-mails or processing received e-mails. MailKit provides support for modern protocols which is why it was used instead of native, maintenance-only **System.Net.Mail** library.

### NuGet Package
[MailKit 3.3.0](https://www.nuget.org/packages/MailKit/)

### Service structure
`IEmailService` includes only exposed, full-package method `Send`:
- `Send(MimeMessage message)` takes already created `MimeMessage` as a parameter. If `From` property is left empty, it will be filled by default sender parameters during the method run.
- `Send(string name, string address, string subject, string body)` allows you to create e-mail on the fly. Should be used only in edge cases, it is preferable to create models.

`IEmailService` is directly implemented only by abstract class `EmailServiceBase`. It includes properties required for successful communication with SMTP server
```
    protected string SmtpServer { get; set; }
    protected int Port { get; set; }
    protected bool UseSSL { get; set; }
    protected string SmtpUsername { get; set; }
    protected string SmtpPassword { get; set; }
    protected MailboxAddress SenderAddress { get; set; }
```
and protected methods called from public `Send` methods. 

`SendMail(MimeMessage message)` handles SMTP server communication by using MailKit specific `SmtpClient` (not the System.Net.Mail one!). 
```
    protected virtual string SendMail(MimeMessage message)
    {
        string response;

        using (var client = new SmtpClient())
        {
            client.ServerCertificateValidationCallback = (s, c, h, e) => true;

            client.Connect(SmtpServer, Port, UseSSL);
            client.Authenticate(SmtpUsername, SmtpPassword);
            response = client.Send(message);
            client.Disconnect(true);
        }

        return response;
    }
```
Response is passed to `ValidateResponse(string response)` which checks if STMP response was *2.0.0 ok*. Due to differing responses based on used SMTP server, this method may be overridden if needed. But right now, it covers all implementations.
```
    protected virtual bool ValidateResponse(string response)
    {
        string expectedOk = "2.0.0 ok";

        if (response.ToLower().StartsWith(expectedOk))
            return true;
        else
            return false;
    }
```
Public `SendMail(MimeMessage message)` method includes try-catch on `SendMail(MimeMessage message)` block which rethrows custom `EmailServiceException`. Because SMTP server communication can provide wild range of exceptions, this solution is taken so user is not exposed to specialized stack traces and potentially sensitive information.
```
    public virtual bool Send(MimeMessage message)
    {
        string response;
        message = BuildMessage(message);
            
        try
        {
            response = SendMail(message);
        }
        catch (Exception e)
        {
            throw new EmailServiceException("There was an error in communication with a mailing server. Your request was aborted.");
        }

        return ValidateResponse(response);
    }
```
Actual implementations then inherit from `EmailServiceBase`, storing their specific parameters in properties and using base methods to process method calls. If needed, there is an option to override parent method with individualized code based on needs of specific SMTP server.

E-mail model should extend `MimeMessage` class and include at least setting of `To` property, `Subject` and of course actual `Body` of the message. `Body` can be configured by `TextFormat` enum to take different types of text (plain text, rich text, HTML).
```
    public MimePasswordReset(string toName, string toEmailAddress, string newPassword)
    {
        To.Add(new MailboxAddress(toName, toEmailAddress));
        Subject = "Your new password";
        Body = new TextPart(MimeKit.Text.TextFormat.Text)
        {
            Text = "There was a request to reset password to your account. You can now sign in by using password: " + newPassword
        };
    }
```

### How To

#### Depedency Injection

Inject *IEmailService* into your class as usual. Right now, interface has two implementations:
- `EmailServiceMailtrap` uses MailTrap web app that allows you to test and debug email sending without reaching actual users.
- `EmailServiceHotmail` uses Hotmail email address to send live e-mails.

Which implementation is added to service provider is handled in `Program.cs` and depends what configuration you run Eucyon-BookIt solution in. *Debug* injects `EmailServiceMailtrap`, *Release* injects `EmailServiceHotmail`.
```
    #if DEBUG
        services.AddScoped<IEmailService, EmailServiceMailtrap>();
    #elif RELEASE
        services.AddScoped<IEmailService, EmailServiceHotmail>();
    #endif
```

#### Using EmailService
Use existing e-mail model or create new one extending `MimeMessage` and pass an instance of it to `SendMail(MimeMessage message)`. Everything else is handled by the service itself.
```
var response = _email.Send(mail);
```

#### Testing
Use `EmailServiceProvider` class to get an instance of testing email service to inject where needed. `EmailServiceProvider` should be never edited to provide any live email service!
### Exception handling
The project relies on a middleware for exception handling. It is done by `ExceptionHandleMiddleware` class implementing `IMiddleware`. As a middleware, `ExceptionHandleMiddleware` processes every request response and then hands it over. But if there is an exception thrown during run of the program, response is intercepted and modified.

Handler sets an instance of `ErrorDetail` as a reponse body and also sets a HTTP status code based on exception type. Personalized `ErrorDetail` is provided by `CreateErrorDetail(Exception exception)` method, which is easily extended to handle different types of exceptions.
```
    public ErrorDetail CreateErrorDetail(Exception exception)
    {
        var errorDetail = new ErrorDetail
        {
            Message = exception.Message,
        };

        switch (exception)
        {
            case InputValidationException:
                errorDetail.StatusCode = (int)HttpStatusCode.BadRequest;
                break;
            default:
                errorDetail.StatusCode = (int)HttpStatusCode.InternalServerError;
                break;
        }

        return errorDetail;
    }
```
`ErrorDetail` is then serialized into JSON and written to HTTP response, overwriting any original response and only exposing user to simple error messages, not detailed stack traces.

### Unit and integration testing
We use [xUnit](https://xunit.net/) library for testing.

#### Endpoint testing
Endpoints are tested by sending a mock request with methods from `WebApplicationFactory` class.
```
    var response = _factory.CreateClient().PostAsync("/api/user/login", JsonContent.Create(loginDto)).Result;
```
For testing purposes, there is a child class `CustomWebApplicationFactory` which is configured to use in-memory Sqlite database instead of MSSQL database used in base project. Make sure to call `TestingContextProvider.CreateContextFromFactory(CustomWebApplicationFactory factory)` in constructor to set up and ensure every test will be run on clean `ApplicationContext`.

#### Dependency injection
Because some tested classes may need specific dependencies, there are multiple provider classes in testing project that supply instances of those dependencies.

`TestingContextProvider` supplies `ApplicationContext`. All Sqlite connections are seeded and configured same way as main context.
- `CreateContextFromScratch()` creates a brand new in-memory Sqlite connection.
- `CreateContextFromFactory(CustomWebApplicationFactory factory)` returns a context based on the connection in `CustomWebApplicationFactory` class.
- `CreateEmptyMockContext()` supplies empty `Mock<ApplicationContext>` that can be configured further or used as a dummy dependency for testing methods that don't access DB data.

`TestingMapperProvider` supplies an instance of `IMapper`. This automapper is configured separately from the one in main project but can use already existing profiles.

`TestingEmailServiceProvider` supplies an instance of `IEmailService`, specifically only `EmailServiceMailtrap` implementation that uses Mailtrap web application. As Mailtrap only collects outgoing e-mails but doesn't actually send them to really users, it should be the only implementation used for testing.

### Swagger UI

Swagger (OpenAPI) is a language-agnostic specification for describing REST APIs. It allows both computers and humans to understand the capabilities of a REST API without direct access to the source code. Its main goals are to:
* Minimize the amount of work needed to connect decoupled services.
* Reduce the amount of time needed to accurately document a service.

#### Usage
To get Swagger open link [http://localhost:5112/swagger].
Json containing API description [https://localhost:5112/swagger/v1/swagger.json].

#### XML Comment
XML comments allows to add additional information. Typical block can contain:
```
/// <summary>
/// Creates a User.
/// </summary>
/// <param name="item"></param>
/// <returns>A newly created User</returns>
/// <remarks>
/// Sample request:
///
///     POST /User
///     {
///        "id": 1,
///        "name": "MrManage",
///        "isActive": true
///     }
///
/// </remarks>
/// <response code="201">Returns the newly created item</response>
/// <response code="400">If the item is null</response>
```

#### Attribute Annotations
Attribute annotations allow additional information to Swagger. It can contain (in combination with XML comment for responses):
```
/// <response code="201">Returns the newly created item</response>
/// <response code="400">If the item is null</response>
[HttpPost]
[ProducesResponseType(StatusCodes.Status201Created)]
[ProducesResponseType(StatusCodes.Status400BadRequest)]
[Produces("application/json")]
etc.
```

#### Additional information
[Intro to Swagger (MS)](https://docs.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-pages-using-swagger)
[SwashBucket (MS)](https://docs.microsoft.com/en-us/aspnet/core/tutorials/getting-started-with-swashbuckle)
[Implementation including JWT](https://www.c-sharpcorner.com/article/authentication-authorization-using-net-core-web-api-using-jwt-token-and/)
[OpenAPI Specification](https://docs.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-pages-using-swagger)

=======
`EnvVariablesMapper` is responsible for setting user secret as environment variables in testing project.
