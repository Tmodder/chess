package service;

import org.mindrot.jbcrypt.BCrypt;
import requestandresult.*;
import dataaccess.*;
import model.User;
import model.Authtoken;

import java.util.UUID;

public class UserService {
    private final UserDAO userDatabase;
    private final AuthDAO authDatabase;

    public UserService(UserDAO userDatabase, AuthDAO authDatabase) {
        this.userDatabase = userDatabase;
        this.authDatabase = authDatabase;
    }

    public RegisterResult registerService(RegisterRequest req) throws ServiceError
    {
        try
        {
            if(userDatabase.findUser(req.username()) != null)
            {
                throw new ServiceError("Error: already taken");
            }

            if(req.password() == null)
            {
                throw new ServiceError("Error: bad request");
            }
            var newUser = new User(req.username(), makeHash(req.password()), req.email());
            userDatabase.createUser(newUser);
            var auth = generateToken();
            var newToken = new Authtoken(auth, req.username());
            authDatabase.createAuth(newToken);
            return new RegisterResult(req.username(),auth);
        }

        catch (DataAccessException e)
        {
            throw new ServiceError(e.getMessage());
        }

    }

    public LoginResult loginService(LoginRequest req) throws ServiceError
    {
        try
        {
            var user = userDatabase.findUser(req.username());
            if (user == null) {
                throw new ServiceError("Error: unauthorized");
            }
            if (!verifyPassword(req.password(), user.password())) {
                throw new ServiceError("Error: unauthorized");
            }
            var auth = generateToken();
            var newToken = new Authtoken(auth, req.username());
            authDatabase.createAuth(newToken);
            return new LoginResult(req.username(),auth);
        }
        catch (DataAccessException e)
        {
            throw new ServiceError(e.getMessage());
        }


    }

    public void logoutService(LogoutRequest req) throws ServiceError {
        try
        {
            var token = authDatabase.findAuth(req.authToken());
            if (token == null)
            {
                throw new DataAccessException("Token is null");
            }
            authDatabase.deleteAuth(token);
        } catch (DataAccessException e) {
            throw new ServiceError("Error: unauthorized");
        }

    }

    private String makeHash(String password)
    {
       return BCrypt.hashpw(password,BCrypt.gensalt());
    }
    private boolean verifyPassword(String password,String hash)
    {
        return BCrypt.checkpw(password, hash);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

