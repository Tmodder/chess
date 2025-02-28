package handler;

import RequestResult.RegisterRequest;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler extends Handler {
    public Object register(Request request, Response response)
    {
        var registerReq = getBody(request, RegisterRequest.class);
        var registerResult = UserService.registerService(registerReq);
        return resultToJson(registerResult);

    }

    public Object login (Request request, Response response)
    {
        return response;
    }

    public Object logout (Request resquest, Response response)
    {
        return response;
    }

}
