package service;
import dataaccess.*;
public class ClearService {
    private final UserDAO userDatabase;
    private final AuthDAO authDatabase;
    private final GameDAO gameDatabase;

    public ClearService(UserDAO userDatabase, AuthDAO authDatabase, GameDAO gameDatabase) {
        this.userDatabase = userDatabase;
        this.authDatabase = authDatabase;
        this.gameDatabase = gameDatabase;
    }

    public void runClear()
    {
        try
        {
            userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }

    }

}
