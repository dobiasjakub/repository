using EucyonBookIt.Middlewares;
using EucyonBookIt.Models;
using Microsoft.AspNetCore.Http;
using System.Text.Json;

namespace HotelManagementTestingProject
{
    public class MiddlewareUnitTests
    {
        [Fact]
        public async Task ExceptionHandlerReturns500StatusCodeAndErrorDetail()
        {
            //Act
            int expectedStatusCode = 500;
            string expectedMessage = "This is an exception thrown in a unit test";

            var expectedObject = new ErrorDetail { StatusCode = expectedStatusCode, Message = expectedMessage };
            var expectedBodyContent = JsonSerializer.Serialize(expectedObject);

            RequestDelegate mockNextMiddleware = innerHttpContext =>
            {
                return Task.FromException(new Exception(expectedMessage));
            };

            var httpContext = new DefaultHttpContext();
            httpContext.Response.ContentType = "application/json";
            httpContext.Response.Body = new MemoryStream();

            var exceptionHandlerMiddleware = new ExceptionHandlerMiddleware();

            //Act
            await exceptionHandlerMiddleware.InvokeAsync(httpContext, mockNextMiddleware);

            int statusCode = httpContext.Response.StatusCode;
            httpContext.Response.Body.Seek(0, SeekOrigin.Begin);
            var bodyContent = new StreamReader(httpContext.Response.Body).ReadToEnd();

            //Assert
            Assert.Equal(expectedStatusCode, statusCode);
            Assert.Equal(expectedBodyContent, bodyContent);
        }

        [Fact]
        public async Task ExceptionHandlerReturns200StatusCodeAndKeepsBodyOverWhenNoExceptionThrown()
        {
            //Act
            var expectedStatusCode = 200;
            var expectedObject = new { Message = "Request handed over to next delegate" };
            var expectedBodyContent = JsonSerializer.Serialize(expectedObject);

            RequestDelegate mockNextMiddleware = innerHttpContext =>
            {
                innerHttpContext.Response.WriteAsync(expectedBodyContent);
                return Task.CompletedTask;
            };

            var httpContext = new DefaultHttpContext();
            httpContext.Response.ContentType = "application/json";
            httpContext.Response.Body = new MemoryStream();

            var exceptionHandlerMiddleware = new ExceptionHandlerMiddleware();
            
            //Act
            await exceptionHandlerMiddleware.InvokeAsync(httpContext, mockNextMiddleware);

            int statusCode = httpContext.Response.StatusCode;
            httpContext.Response.Body.Seek(0, SeekOrigin.Begin);
            var bodyContent = new StreamReader(httpContext.Response.Body).ReadToEnd();

            //Assert
            Assert.Equal(expectedStatusCode, statusCode);
            Assert.Equal(expectedBodyContent, bodyContent);
        }
    }
}
