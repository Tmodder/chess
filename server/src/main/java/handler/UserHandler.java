package handler;

import requestandresult.LoginRequest;
import requestandresult.LogoutRequest;
import requestandresult.RegisterRequest;
import dataaccess.DataAccessException;
import service.ServiceError;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler extends Handler {
    public Object register(Request request, Response response)
    {
        try {
            var registerReq = getBody(request, RegisterRequest.class);
            var registerResult = UserService.registerService(registerReq);
            String json = resultToJson(registerResult);
            return json;
        }

        catch (ServiceError error)
        {
            return ErrorHandler.handleServiceError(error,response);
        }


    }

    public Object login (Request request, Response response)
    {
        try {
            var loginReq = getBody(request, LoginRequest.class);
            var loginResult = UserService.loginService(loginReq);
            String json = resultToJson(loginResult);
            return json;
        }
        catch (ServiceError error)
        {
            return ErrorHandler.handleServiceError(error,response);
        }
    }

    public Object logout (Request request, Response response) throws DataAccessException {
        try
        {
            UserService.logoutService(new LogoutRequest(request.headers("authorization")));
            return "null";
        } catch (ServiceError error) {

            return ErrorHandler.handleServiceError(error,response);
        }
    }

}
