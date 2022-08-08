using EucyonBookIt.Resources;
using EucyonBookIt.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Localization;

namespace EucyonBookIt.Controllers
{
    [Route("api/manager")]
    [ApiController]
    [Authorize (Roles ="Manager")]
    public class ManagerController : ControllerBase
    {
        private readonly IManagerService _service;
        private readonly IStringLocalizer<StringResource> _localizer;

        public ManagerController(IManagerService managerService, IStringLocalizer<StringResource> localizer)
        {
            _service = managerService;
            _localizer = localizer;
        }

        /// <summary>
        /// Upcoming reservations for all managed Hotels by Hotel manager
        /// </summary>
        /// <returns>List of reservations with all reservation details included. Reservations will be sorted by Reservation date from nearest</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     Get /reservations/
        ///
        /// </remarks>
        /// <response code="200">Returns when request is sent from valid account with role == manager, after authentification .</response>
        /// <response code="401">Returns when request is sent from account with no authorization</response>
        /// <response code="403">Returns when request is sent from account with bad authorization level for ex.(role == customer).</response>
        [HttpGet("reservations/")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [Produces("application/json")]
        public IActionResult GetReservations()
        {
            string email = HttpContext.User.Identity.Name;
            var listOfReservations = _service.GetHotelReservationsByManagerEmail(email);
            return listOfReservations != null ? Ok(listOfReservations) : Unauthorized(new { Message = _localizer["AuthorizationNeeded"].Value });
        }

        /// <summary>
        /// Upcoming reservations for all managed Hotels by Hotel manager, sorted by Hotel, then by date ReservationStart
        /// </summary>
        /// <returns>List of Hotels. There, for each room reservations are shown in order from nearest Reservation start date.</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     Get /reservations/hotels/
        ///
        /// </remarks>
        /// <response code="200">Returns when request is sent from valid account with role == manager, after authentification .</response>
        /// <response code="401">Returns when request is sent from account with no authorization</response>
        /// <response code="403">Returns when request is sent from account with bad authorization level for ex.(role == customer).</response>
        [HttpGet("reservations/hotels")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [Produces("application/json")]
        public IActionResult GetReservationsInHotels()
        {
            string email = HttpContext.User.Identity.Name;  

            var listOfReservations = _service.GetReservationsByHotelsByManagerEmail(email);
            return listOfReservations != null ? Ok(listOfReservations) : Unauthorized(new { Message = _localizer["AuthorizationNeeded"].Value });
        }
    }
}
