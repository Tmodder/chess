package service;
import dataaccess.*;
public class ClearService {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();
    private static final GameDAO gameDatabase = GameDAO.makeInstance();
    public static void clear()
    {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();
    }

}
