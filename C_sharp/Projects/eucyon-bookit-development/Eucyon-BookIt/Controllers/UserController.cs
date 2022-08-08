using AutoMapper;
using EucyonBookIt.Models.DTOs;
using EucyonBookIt.Models;
using EucyonBookIt.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.Extensions.Localization;
using EucyonBookIt.Resources;

namespace EucyonBookIt.Controllers
{
    [Route("api/user")]
    [ApiController]
    [Authorize]
    public class UserController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly IAuthService _authService;
        private readonly IMapper _mapper;
        private readonly IStringLocalizer<StringResource> _localizer;

        public UserController(IUserService userService, IAuthService authService, IMapper mapper, IStringLocalizer<StringResource> localizer)
        {
            _userService = userService;
            _authService = authService;
            _mapper = mapper;
            _localizer = localizer;
        }

        /// <summary>
        /// Register new User
        /// </summary>
        /// <returns>Confirmation that new user was registered.</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     POST /register
        ///     {
        ///         "EmailAddress" : "some@email.com",
        ///         "Password" : "SomePassword123",
        ///         "PasswordRetype" : "SomePassword123",
        ///         "Role" : "Customer"
        ///     }
        ///
        /// </remarks>
        /// <response code="200">Returns confirmation that user was registered.</response>
        /// <response code="400">Returns when user was not registered.</response>
        [HttpPost("registration")]
        [AllowAnonymous]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status400BadRequest)]
        [Produces("application/json")]
        public IActionResult SignUp(UserRegistrationDTO dto)
        {
            var result = _userService.RegisterUser(dto);
            return result != null ? Ok(new BaseResponseDTO(_localizer["RegistrationSuccessful"])) 
                : BadRequest(new BaseResponseDTO(_localizer["RegistrationFailure"]));
        }

        /// <summary>
        /// Login and get an authentication token
        /// </summary>
        /// <returns>Message that new user was registered and a JWT authentication token.</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     POST /login
        ///     {
        ///         "EmailAddress" : "some@email.com",
        ///         "Password" : "SomePassword123",
        ///     }
        ///
        /// </remarks>
        /// <response code="200">Returns when user was with provided EmailAddress and Password combination was found and authentication token generated.</response>
        /// <response code="400">Returns when provided combination doesn't match any existing user.</response>
        [HttpPost("login")]
        [AllowAnonymous]
        [ProducesResponseType(typeof(UserLoginSuccessDTO),StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status400BadRequest)]
        [Produces("application/json")]
        public IActionResult LoginIn(UserLoginDTO dto)
        {
            var result = _userService.LoginUser(dto);
            if (result == null)
                return BadRequest(new BaseResponseDTO(_localizer["LoginFailure"]));

            var token = _authService.CreateToken(result);
            return Ok(new UserLoginSuccessDTO(_localizer["LoginSuccessful"], token));
        }

        /// <summary>
        /// Reset password
        /// </summary>
        /// <returns>Message that the password was reset and a new one sent by e-mail</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     POST /login
        ///     {
        ///         "EmailAddress" : "some@email.com",
        ///     }
        ///
        /// </remarks>
        /// <response code="200">Returns when user with provided email address was found and the password reset.</response>
        /// <response code="400">Returns when provided email address doesn't match any existing users.</response>
        [HttpPost("resetpassword")]
        [AllowAnonymous]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [Produces("application/json")]
        public IActionResult ResetPassword(ResetPasswordDTO dto)
        {
            var result = _userService.ResetPassword(dto);
            return result != null ? Ok(new BaseResponseDTO(_localizer["PasswordResetSuccess"])) 
                : BadRequest(new BaseResponseDTO(_localizer["PasswordResetFailure"]));
        }

        /// <summary>
        /// Endpoint returns all registered users. Admins only.
        /// </summary>
        /// <returns>Return List of User DTOs with values that can be edited.</returns>
        /// <response code="200">Returns when user is authorized as Admin.</response>
        /// <response code="401">Returns when user is not authorized.</response>
        /// <response code="403">Returns when user is not authorized as Admin.</response>
        [HttpGet("getusers")]
        [ProducesResponseType(typeof(List<UserEditDTO>), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [Authorize(Roles = "Admin")]
        [Produces("application/json")]
        public IActionResult GetUsers()
        {
            return Ok(new { Users = _mapper.Map<List<UserEditDTO>>(_userService.GetUsers()) });
        }

        /// <summary>
        /// Get editable information related to authorized user
        /// </summary>
        /// <returns>Information related to the user.</returns>
        /// <response code="200">Returns when user is authorized and found in the DB.</response>
        /// <response code="401">Returns when user is not authorized.</response>
        /// <response code="404">Returns when user was not found in the DB.</response>
        [HttpGet("")]
        [ProducesResponseType(typeof(UserEditDTO), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status404NotFound)]
        [Produces("application/json")]
        public IActionResult GetEditableInfo()
        {
            string? emailAddress = HttpContext.User.Identity.Name;
                        
            User? userFromDB = _userService.GetUserBy(emailAddress, false);
            if (userFromDB == null || userFromDB.UserId < 1)
            {
                return NotFound(new BaseResponseDTO("User not found in DB."));
            }

            // Load User details from DB including Person details
            userFromDB = _userService.GetUserBy(userFromDB);
            UserEditDTO editableValues = _mapper.Map<UserEditDTO>(userFromDB);

            return Ok(editableValues);
        }

        /// <summary>
        /// Put editable information related to authorized user
        /// </summary>
        /// <remarks>
        /// Sample request:
        ///
        ///     PUT /edit
        ///     {
        ///     "userId": 2,
        ///     "emailAddress": "MrManage@holidaywin.com",
        ///     "password": "Password123",
        ///     "newPassword": null, (optional)
        ///     "personFirstName": "Max",
        ///     "personLastName": "Wayne"
        ///     }
        ///
        /// </remarks>
        /// <returns>Information related to the user.</returns>
        /// <response code="200">Returns when user is authorized and found in the DB.</response>
        /// <response code="400">Returns when insufficient info is provided.</response>
        /// <response code="401">Returns when user is not authorized.</response>
        /// <response code="404">Returns when user is found in the DB.</response>
        [HttpPut("edit")]
        [ProducesResponseType(typeof(UserEditDTO), StatusCodes.Status200OK)]
        [Produces("application/json")]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status400BadRequest)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status404NotFound)]
        public IActionResult PutEditableInfo(UserEditDTO userEditDTO)
        {
            if (userEditDTO == null)
            {
                return BadRequest(new BaseResponseDTO("No info provided!"));
            }

            if (!((userEditDTO.EmailAddress != HttpContext.User.Identity.Name) || !HttpContext.User.IsInRole("Admin")))
            {
                return BadRequest(new BaseResponseDTO("Insufficient rights for edit!"));
            }

            User userFromDB = _userService.GetUserBy(userEditDTO.EmailAddress, userEditDTO.Password);

            if (userFromDB == null || !(userFromDB.UserId > 0))
            {
                return NotFound(new BaseResponseDTO("User not found in DB!"));
            }

            // Load User details from DB including Person details
            User userToUpdate = _userService.GetUserBy(userFromDB);
            userToUpdate.Person.FirstName = userEditDTO.PersonFirstName;
            userToUpdate.Person.LastName = userEditDTO.PersonLastName;
            userToUpdate.Password = userEditDTO.NewPassword == null ? userEditDTO.Password : userEditDTO.NewPassword;
            userToUpdate.Role.RoleName = HttpContext.User.IsInRole("Admin") ? userEditDTO.RoleRoleName : userToUpdate.Role.RoleName;

            return _userService.UpdateUser(userToUpdate) ? Ok(_mapper.Map<UserEditDTO>(userToUpdate)) : BadRequest(new BaseResponseDTO("User cannot be updated!"));
        }

        /// <summary>
        /// Patch editable information related to user's email /edit/{email}.
        /// If email is not filled, then requestor's email is used.
        /// </summary>
        /// <remarks>
        /// Sample request:
        ///
        ///     PATCH /edit/{email}
        ///     [
        ///         {
        ///         "op": "replace",
        ///         "path": "/PersonFirstname",
        ///         "value": "Sapo"
        ///         }
        ///     ]
        ///
        /// </remarks>
        /// <returns>Information related to the user.</returns>
        /// <response code="200">Returns when user is authorized and found in the DB.</response>
        /// <response code="400">Returns when insufficient info is provided.</response>
        /// <response code="401">Returns when user is not authorized.</response>
        /// <response code="404">Returns when user is found in the DB.</response>
        [HttpPatch("edit/{email}")]
        [ProducesResponseType(typeof(UserEditDTO), StatusCodes.Status200OK)]
        [Produces("application/json")]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status400BadRequest)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(typeof(BaseResponseDTO), StatusCodes.Status404NotFound)]
        public IActionResult PatchData([FromRoute] string? email, [FromBody] JsonPatchDocument<UserEditDTO> patchUser)
        {
            email = email ?? HttpContext.User.Identity.Name;
            User? userFromDb = _userService.GetUserBy(email, false);

            if (userFromDb == null)
            {
                return NotFound(new BaseResponseDTO("Incorrect username!"));
            }

            if (!((userFromDb.EmailAddress == HttpContext.User.Identity.Name) || HttpContext.User.IsInRole("Admin")))
            {
                return BadRequest(new BaseResponseDTO("Insufficient rights for edit!"));
            }

            string password = userFromDb.Password;
            // Load User included Person
            userFromDb = _userService.GetUserBy(userFromDb);

            string role = userFromDb.Role.RoleName;

            UserEditDTO userEditDTO = _mapper.Map<UserEditDTO>(userFromDb);

            patchUser.ApplyTo(userEditDTO, ModelState);

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (userEditDTO.Password != password)
            {
                return BadRequest(new BaseResponseDTO("Edit Password with PUT method only!" ));
            }

            if (userEditDTO.RoleRoleName != role)
            {
                return BadRequest(new BaseResponseDTO("Edit Role is permited to Admin role only!"));
            }

            // Updates existing object with changes
            userFromDb = _mapper.Map<UserEditDTO, User>(userEditDTO, userFromDb);

            return _userService.UpdateUser(userFromDb) ? Ok(userEditDTO) : BadRequest(new BaseResponseDTO("User cannot be updated!"));
        }
    }
}