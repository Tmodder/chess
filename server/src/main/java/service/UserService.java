package service;

import RequestResult.*;
import dataaccess.*;
import model.User;
import model.Authtoken;
import java.util.UUID;

public class UserService {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();
    public static RegisterResult registerService(RegisterRequest req)
    {
        if(userDatabase.findUser(req.username()) == null)
        {
            var newUser = new User(req.username(), req.password(), req.email());
            userDatabase.createUser(newUser);
            var auth = generateToken();
            var newToken = new Authtoken(auth, req.username());
            authDatabase.createAuth(newToken);
            return new RegisterResult(req.username(),auth);
        }
        return null;
    }

    public static LoginResult loginService(LoginRequest req)
    {
        var user = userDatabase.findUser(req.username());
        // if not null
        if (user.password().equals(req.password()))
        {
            var auth = generateToken();
            var newToken = new Authtoken(auth, req.username());
            authDatabase.createAuth(newToken);
            return new LoginResult(auth);
        }
        return null;

    }

    public static void logoutService(LogoutRequest req) throws DataAccessException {
        var token = authDatabase.findAuth(req.authToken());
        if (token != null)
        {
            authDatabase.deleteAuth(token);
        }
    }



    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
