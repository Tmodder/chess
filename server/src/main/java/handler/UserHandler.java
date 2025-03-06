package handler;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import requestandresult.LoginRequest;
import requestandresult.LogoutRequest;
import requestandresult.RegisterRequest;
import dataaccess.DataAccessException;
import service.ServiceError;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler extends Handler {
    private final UserService service;

    public UserHandler(UserDAO userDatabase, AuthDAO authDatabase) {
        this.service = new UserService(userDatabase, authDatabase);

    }

    public Object register(Request request, Response response)
    {
        try {
            var registerReq = getBody(request, RegisterRequest.class);
            var registerResult = service.registerService(registerReq);
            return resultToJson(registerResult);
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
            var loginResult = service.loginService(loginReq);
            return resultToJson(loginResult);
        }
        catch (ServiceError error)
        {
            return ErrorHandler.handleServiceError(error,response);
        }
    }

    public Object logout (Request request, Response response) throws DataAccessException {
        try
        {
            service.logoutService(new LogoutRequest(request.headers("authorization")));
            return "null";
        } catch (ServiceError error) {

            return ErrorHandler.handleServiceError(error,response);
        }
    }

}
