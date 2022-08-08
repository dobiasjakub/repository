using System.Runtime.Serialization;

namespace EucyonBookIt.Models.Exceptions
{
    public class EmailServiceException : Exception
    {
        public EmailServiceException()
        {
        }

        public EmailServiceException(string? message) : base(message)
        {
        }

        public EmailServiceException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected EmailServiceException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}
