public class ResponseException extends RuntimeException
{
    private final int statusCode;
    public ResponseException(int code, String message)
    {
        super(message);
        statusCode = code;

    }
    public int getStatus()
    {
        return statusCode;
    }
}
