using EucyonBookIt.Resources;
using EucyonBookIt.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Localization;

namespace EucyonBookIt.Controllers
{
    [Route("api/hotel")]
    [ApiController]
    [Authorize]
    public class HotelController : ControllerBase
    {
        private readonly IHotelService _hotelService;
        private readonly IStringLocalizer<StringResource> _localizer;

        public HotelController(IHotelService hotelService, IStringLocalizer<StringResource> localizer)
        {
            this._hotelService = hotelService;
            _localizer = localizer;
        }

        /// <summary>
        /// Details of hotel by hotelId
        /// </summary>
        /// <returns>Hotel DTO by Id with all hotel details included</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     Get /id/{id}
        ///
        /// </remarks>
        /// <response code="200">Returns when request is sent from valid account and if hotel exists by dedicated Id.</response>
        /// <response code="400">Returns when Hotel is not found by dedicated Id</response>
        /// <response code="401">Returns when request is sent from account with no authorization</response>
        [HttpGet("id/{id}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [Produces("application/json")]
        public IActionResult GetDetailsById(long id)
        {
            var hotelDetailsDto = _hotelService.GetHotelDetailsById(id);
            return hotelDetailsDto != null ? Ok(hotelDetailsDto) : NotFound(new { Message = _localizer["HotelNotFound"].Value });
        }

        /// <summary>
        /// Details of hotel by Hotel name and Location
        /// </summary>
        /// <returns>Hotel DTO with all details included by combination of Hotel name and it´s location</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     Get /locationAndName/{location}/{name}
        ///
        /// </remarks>
        /// <response code="200">Returns when request is sent from authentificated account and hotel exists by name and location.</response>
        /// <response code="400">Returns when Hotel is not found by dedicated Location and Name.</response>
        /// <response code="401">Returns when request is sent with no authorization</response>
        [HttpGet("locationAndName/{location}/{name}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [Produces("application/json")]
        public IActionResult GetDetailsByLocationAndName(string location, string name)
        {
            var hotelDetailsDto = _hotelService.GetHotelByLocationAndName(location,name);
            return hotelDetailsDto != null ? Ok(hotelDetailsDto) : NotFound(new { Message = _localizer["HotelNotFound"].Value });
        }

        /// <summary>
        /// All hotels with details in dedicated location
        /// </summary>
        /// <returns>List of HotelDetails with all hotels in dedicated location</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     Get /locationAndName/{location}/details
        ///
        /// </remarks>
        /// <response code="200">Returns when request is sent from valid account and hotel exists in selected location. Authentification needed first.</response>
        /// <response code="400">Returns when no Hotel is found in selected Location.</response>
        /// <response code="401">Returns when request is sent with no authorization</response>
        [HttpGet("hotels/location/{location}/details")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [Produces("application/json")]
        public IActionResult GetHotelsByLocation(string location)
        {
            var listOfHotelsWithDetails = _hotelService.GetHotelsByLocation(location);
            return listOfHotelsWithDetails.Hotels.Count != 0 ? Ok(listOfHotelsWithDetails) : NotFound(new { Message = _localizer["HotelNoneInLocation"].Value });
        }

        /// <summary>
        /// List of hotel names located in dedicated location
        /// </summary>
        /// <returns>List with names of all hotels located in dedicated location</returns>
        /// <remarks>
        /// Sample request:
        ///
        ///     Get hotels/location/{location}/names
        ///
        /// </remarks>
        /// <response code="200">Returns when request is sent from valid account and hotel exists in selected location. Authentification needed first.</response>
        /// <response code="400">Returns when no Hotel is found in selected Location.</response>
        /// <response code="401">Returns when request is sent with no authorization</response>

        [HttpGet("hotels/location/{location}/names")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [Produces("application/json")]
        public IActionResult GetHotelNamesByLocation(string location)
        {
            var listOfHotelNames = _hotelService.GetHotelNamesByLocation(location);
            return listOfHotelNames.HotelNames.Count != 0 ? Ok(listOfHotelNames) : NotFound(new { Message = _localizer["HotelNoneInLocation"].Value });
        }
    }
}
