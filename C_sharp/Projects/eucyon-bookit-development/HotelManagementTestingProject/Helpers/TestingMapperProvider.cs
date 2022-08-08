using AutoMapper;
using EucyonBookIt.Profiles;

namespace HotelManagementTestingProject.Helpers
{
    public static class TestingMapperProvider
    {
        public static IMapper CreateAutomapper()
        {
            var config = new MapperConfiguration(mc =>
            {
                mc.AddProfile(new UserProfiles());
                mc.AddProfile(new HotelProfiles());
                mc.AddProfile(new ReservationProfile());
            });

            IMapper mapper = config.CreateMapper();

            return mapper;
        }
    }
}
