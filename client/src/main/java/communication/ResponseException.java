package communication;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ResponseException extends RuntimeException
{
    private final int statusCode;
    public ResponseException(int code, String message)
    {
        super(message);
        statusCode = code;

    }
    public static ResponseException fromJson(InputStream stream,int status) {
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        String message = map.get("message").toString();
        return new ResponseException(status, message);
    }
    public int getStatus()
    {
        return statusCode;
    }
}
