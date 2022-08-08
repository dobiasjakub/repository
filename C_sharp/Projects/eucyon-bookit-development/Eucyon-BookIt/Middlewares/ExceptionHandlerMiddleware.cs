using EucyonBookIt.Models;
using EucyonBookIt.Models.Exceptions;
using System.Net;
using System.Text.Json;

namespace EucyonBookIt.Middlewares
{
    public class ExceptionHandlerMiddleware : IMiddleware
    {
        public async Task InvokeAsync(HttpContext context, RequestDelegate next)
        {
            try
            {
                await next(context);
            }
            catch (Exception exception)
            {
                var errorDetail = CreateErrorDetail(exception);
                
                var response = context.Response;
                response.ContentType = "application/json";
                response.StatusCode = errorDetail.StatusCode;

                var result = JsonSerializer.Serialize(errorDetail);
                await response.WriteAsync(result);
            }
        }

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
                case EmailServiceException:
                    errorDetail.StatusCode = (int)HttpStatusCode.InternalServerError;
                    break;
                default:
                    errorDetail.StatusCode = (int)HttpStatusCode.InternalServerError;
                    break;
            }

            return errorDetail;
        }
    }
}
