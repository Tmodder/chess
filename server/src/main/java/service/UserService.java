package service;

import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import dataaccess.MemoryUserDAO;
import model.User;
import model.Authtoken;
import java.util.UUID;
import dataaccess.UserDAO;

public class UserService {
    private static UserDAO userDatabase = new MemoryUserDAO();
    public static RegisterResult registerService(RegisterRequest req)
    {
        if(userDatabase.findUser(req.username()) != null)
        {
            userDatabase.createUser(req.username(),req.password(),req.email());
            //TODO add authDatabase implementation
            var auth = generateToken();
            return new RegisterResult(req.username(),auth);
        }
        return null;
    }



//    public static Authtoken createAuth()
//    {
//        generateToken();
//        return null;
//    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
