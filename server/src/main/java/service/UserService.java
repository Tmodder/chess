package service;

import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.User;
import model.Authtoken;
import java.util.UUID;
import dataaccess.UserDAO;

public class UserService {
    private static final UserDAO userDatabase = new MemoryUserDAO();
    private static final AuthDAO authDatabase = new MemoryAuthDAO();
    public static RegisterResult registerService(RegisterRequest req)
    {
        if(userDatabase.findUser(req.username()) != null)
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


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
