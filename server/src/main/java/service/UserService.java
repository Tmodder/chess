package service;

import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import model.User;
import model.Authtoken;
import java.util.UUID;


public class UserService {
    public static RegisterResult registerService(RegisterRequest req)
    {
        // getUser()
        // if null
        // createUser()
        // createAuth()
        // return new RegisterResult()
        return null;
    }

    public static User getUser(String username)
    {

        return null;
    }

    public static User createUser()
    {
        return null;
    }

    public static Authtoken createAuth()
    {
        generateToken();
        return null;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
