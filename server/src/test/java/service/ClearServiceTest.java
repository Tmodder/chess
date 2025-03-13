package service;

import dataaccess.*;
import requestandresult.CreateGameRequest;
import requestandresult.RegisterRequest;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private final AuthDAO authDatabase = new MemoryAuthDAO();
    private final UserDAO userDatabase = new MemoryUserDAO();
    private final UserService user = new UserService(userDatabase, authDatabase);
    private final GameDAO gameDatabase = new MemoryGameDAO();
    private final GameService game = new GameService(authDatabase,gameDatabase);
    private final ClearService service = new ClearService(userDatabase, authDatabase, gameDatabase);
    static String authToken;
    static String username = "bob";
    public void fillDb()
    {
        try
        {
            userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
            var registerRes = user.registerService(new RegisterRequest(username,"password","email"));
            authToken = registerRes.authToken();
            game.createGame(new CreateGameRequest(authToken,"bobs game"));
        }
        catch (DataAccessException e)
        {
            throw new ServiceError(e.getMessage());
        }
    }

    @Test
    public void clearFullDb()
    {
        try {
            fillDb();
            service.runClear();
            assertEquals(0, gameDatabase.getGamesList().size());
            assertNull(authDatabase.findAuth(authToken));
            assertNull(userDatabase.findUser(username));
        }
        catch (DataAccessException e)
        {
            throw new ServiceError(e.getMessage());
        }
    }
}
