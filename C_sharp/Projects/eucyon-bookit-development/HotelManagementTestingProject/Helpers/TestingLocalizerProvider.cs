using EucyonBookIt.Resources;
using Microsoft.Extensions.Localization;
using Microsoft.Extensions.Logging.Abstractions;
using Microsoft.Extensions.Options;

namespace HotelManagementTestingProject.Helpers
{
    public static class TestingLocalizerProvider
    {
        public static IStringLocalizer<StringResource> GetLocalizer()
        {
            var options = Options.Create(new LocalizationOptions());
            var factory = new ResourceManagerStringLocalizerFactory(options, NullLoggerFactory.Instance);

            return new StringLocalizer<StringResource>(factory);
        }
    }
}
