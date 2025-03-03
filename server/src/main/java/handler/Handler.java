package handler;

import com.google.gson.Gson;
import spark.Request;

public class Handler {
    public static <T> T getBody(Request request, Class<T> outputClass)
    {
        return new Gson().fromJson(request.body(),outputClass);

    }

//    public static <T> T getBodyWithAuth(Request request, Class<T> outputClass)
//    {
//        String authToken = request.headers("authorization");
//        String newString = request.body().substring(0,4) + "\"authToken\": \"" + authToken + "\"\n  \"" +
//                request.body().substring(5);
//        return new Gson().fromJson(newString.trim(),outputClass);
//
//    }

    public static <T> String resultToJson(T result)
    {
        return new Gson().toJson(result);
    }
}
