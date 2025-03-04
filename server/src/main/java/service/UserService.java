package service;

import RequestResult.*;
import dataaccess.*;
import model.User;
import model.Authtoken;
import java.util.UUID;

public class UserService {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();

    public static RegisterResult registerService(RegisterRequest req) throws ServiceError
    {
        if(userDatabase.findUser(req.username()) != null)
            throw new ServiceError("Error: already taken");
        if(req.password() == null)
            throw new ServiceError("Error: bad request");
        var newUser = new User(req.username(), req.password(), req.email());
        userDatabase.createUser(newUser);
        var auth = generateToken();
        var newToken = new Authtoken(auth, req.username());
        authDatabase.createAuth(newToken);
        return new RegisterResult(req.username(),auth);
    }

    public static LoginResult loginService(LoginRequest req) throws ServiceError
    {
        var user = userDatabase.findUser(req.username());
        if (user == null)
            throw new ServiceError("Error: unauthorized");
        if (!user.password().equals(req.password()))
            throw new ServiceError("Error: unauthorized");
        var auth = generateToken();
        var newToken = new Authtoken(auth, req.username());
        authDatabase.createAuth(newToken);
        return new LoginResult(req.username(),auth);

    }

    public static void logoutService(LogoutRequest req) throws ServiceError,DataAccessException {
        var token = authDatabase.findAuth(req.authToken());
        if (token == null)
            throw new ServiceError("Error: unauthorized");
        authDatabase.deleteAuth(token);
    }



    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

