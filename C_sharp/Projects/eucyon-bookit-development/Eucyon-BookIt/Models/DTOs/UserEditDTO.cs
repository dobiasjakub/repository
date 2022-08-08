namespace EucyonBookIt.Models.DTOs
{
    public class UserEditDTO
    {
        public long? UserId { get; set; }
        public string EmailAddress { get; set; }
        public string Password { get; set; }
        public string? NewPassword { get; set; }
        public string PersonFirstName { get; set; }
        public string PersonLastName { get; set; }
        public string RoleRoleName { get; set; }
    }
}