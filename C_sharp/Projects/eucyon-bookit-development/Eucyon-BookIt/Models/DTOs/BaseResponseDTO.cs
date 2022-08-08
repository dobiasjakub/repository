namespace EucyonBookIt.Models.DTOs
{
    public class BaseResponseDTO
    {
        public string Message { get; set; } = string.Empty;

        public BaseResponseDTO(string message)
        {
            Message = message;
        }
    }
}
