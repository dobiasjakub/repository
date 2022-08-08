using System.Runtime.Serialization;

namespace EucyonBookIt.Models.Exceptions
{
    public class InputValidationException : Exception
    {
        public InputValidationException()
        {
        }

        public InputValidationException(string? message) : base(message)
        {
        }

        public InputValidationException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected InputValidationException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}
