package service;
import dataaccess.*;
public class ClearService {
    private final UserDAO USER_DATABASE;
    private final AuthDAO AUTH_DATABASE;
    private final GameDAO GAME_DATABASE;

    public ClearService(UserDAO USER_DATABASE, AuthDAO AUTH_DATABASE, GameDAO GAME_DATABASE) {
        this.USER_DATABASE = USER_DATABASE;
        this.AUTH_DATABASE = AUTH_DATABASE;
        this.GAME_DATABASE = GAME_DATABASE;
    }

    public void runClear()
    {
        try
        {
            USER_DATABASE.clear();
            AUTH_DATABASE.clear();
            GAME_DATABASE.clear();
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }

    }

}
