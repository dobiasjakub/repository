namespace EucyonBookIt.Models.DTOs
{
    public class ListOfHotelDetailsDTO
    {
        public string Location { get; set; }
        public List<HotelDetailsDTO> Hotels { get; set; }
    }
}
