using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace EucyonBookIt.Swagger
{
    public class AcceptLanguageFilter : IOperationFilter
    {
        public void Apply(OpenApiOperation operation, OperationFilterContext context)
        {
            if (operation.Parameters == null)
                operation.Parameters = new List<OpenApiParameter>();

            operation.Parameters.Add(new OpenApiParameter
            {
                Name = "Accept-Language",
                In = ParameterLocation.Header,
                Description = "Request language (en/cs)",
                Required = false,

                Schema = new OpenApiSchema
                {
                    Type = "String",
                    Default = new OpenApiString("en")
                }
            });
        }
    }
}
