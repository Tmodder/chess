package handler;

import com.google.gson.Gson;
import spark.Request;
import service.ServiceError;

import java.util.HashMap;
import java.util.Map;

public class Handler {
    public static <T> T getBody(Request request, Class<T> outputClass)
    {
        return new Gson().fromJson(request.body(),outputClass);

    }

    public static <T> String resultToJson(T result)
    {
        return new Gson().toJson(result);
    }

    public static String exceptionToJson(ServiceError error)
    {
        Gson gson = new Gson();
        HashMap<String,String> errorMap = new HashMap<>();
        errorMap.put("message",error.getMessage());
        return gson.toJson(errorMap);
    }
}
