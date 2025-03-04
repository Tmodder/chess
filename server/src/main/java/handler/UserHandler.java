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
            String message = error.getMessage();
            if (message.equals("Error: bad request"))
            {
                response.status(400);
            } else if (message.equals("Error: already taken")) {
                response.status(403);
            }
            else {
                response.status(500);
            }
            return exceptionToJson(error);
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
            String message = error.getMessage();
            if (message.equals("Error: unauthorized"))
            {
                response.status(401);
            } else {
                response.status(500);
            }
            return exceptionToJson(error);
        }
    }

    public Object logout (Request request, Response response) throws DataAccessException {
        try
        {
            UserService.logoutService(new LogoutRequest(request.headers("authorization")));
            return "null";
        } catch (ServiceError e) {
            String message = e.getMessage();
            if (message.equals("Error: unauthorized"))
            {
                response.status(401);
            }
            else {
                response.status(500);
            }
            return exceptionToJson(e);
        }
    }

}
