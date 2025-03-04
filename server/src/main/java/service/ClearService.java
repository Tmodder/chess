package service;
import dataaccess.*;
public class ClearService {
    private static final UserDAO USER_DATABASE = UserDAO.makeInstance();
    private static final AuthDAO AUTH_DATABASE = AuthDAO.makeInstance();
    private static final GameDAO GAME_DATABASE = GameDAO.makeInstance();
    public static void runClear()
    {
        USER_DATABASE.clear();
        AUTH_DATABASE.clear();
        GAME_DATABASE.clear();
    }

}
