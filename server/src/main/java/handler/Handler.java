package handler;

import com.google.gson.Gson;
import spark.Request;

public class Handler {
    public static <T> T getBody(Request request, Class<T> outputClass)
    {
        return new Gson().fromJson(request.body(),outputClass);

    }
    public static <T> String resultToJson(T result)
    {
        return new Gson().toJson(result);
    }
}
