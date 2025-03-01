package handler;

import RequestResult.LoginRequest;
import RequestResult.LogoutRequest;
import RequestResult.RegisterRequest;
import dataaccess.DataAccessException;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler extends Handler {
    public Object register(Request request, Response response)
    {
        var registerReq = getBody(request, RegisterRequest.class);
        var registerResult = UserService.registerService(registerReq);
        String json = resultToJson(registerResult);
        if (json.equals("null"))
        {
            response.status(500);
        }
        return json;

    }

    public Object login (Request request, Response response)
    {
       var loginReq = getBody(request, LoginRequest.class);
       var loginResult = UserService.loginService(loginReq);
       String json = resultToJson(loginResult);
        if (json.equals("null"))
        {
            response.status(500);
        }
        return json;
    }

    public Object logout (Request request, Response response) throws DataAccessException {
        UserService.logoutService(new LogoutRequest(request.headers("authorization")));
        return "null";
    }

}
