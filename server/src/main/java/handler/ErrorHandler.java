package handler;

import com.google.gson.Gson;
import service.ServiceError;
import spark.Response;

import java.util.HashMap;

public class ErrorHandler
{
    public static String handleServiceError(ServiceError e,Response response)
    {
        String message = e.getMessage();
        if (message.equals("Error: bad request"))
        {
            response.status(400);
        }
        else if (message.equals("Error: unauthorized"))
        {
            response.status(401);
        }
        else if (message.equals("Error: already taken")) {
            response.status(403);
        }
        else {
            response.status(500);
        }
        return exceptionToJson(e);
    }

    public static String exceptionToJson(ServiceError error)
    {
        Gson gson = new Gson();
        HashMap<String,String> errorMap = new HashMap<>();
        errorMap.put("message",error.getMessage());
        return gson.toJson(errorMap);
    }
}
