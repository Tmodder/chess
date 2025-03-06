package service;

import dataaccess.*;
import requestandresult.CreateGameRequest;
import requestandresult.RegisterRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private final AuthDAO AUTH_DATABASE = new MemoryAuthDAO();
    private final UserDAO USER_DATABASE = new MemoryUserDAO();
    private final UserService user = new UserService(USER_DATABASE, AUTH_DATABASE);
    private final GameDAO GAME_DATABASE = new MemoryGameDAO();
    private final GameService game = new GameService(AUTH_DATABASE,GAME_DATABASE);
    private final ClearService service = new ClearService(USER_DATABASE, AUTH_DATABASE, GAME_DATABASE);
    static String authToken;
    static String username = "bob";
    public void fillDb()
    {
        USER_DATABASE.clear();
        AUTH_DATABASE.clear();
        GAME_DATABASE.clear();
        var registerRes = user.registerService(new RegisterRequest(username,"password","email"));
        authToken = registerRes.authToken();
        game.createGame(new CreateGameRequest(authToken,"bobs game"));
    }

    @Test
    public void clearFullDb()
    {
        fillDb();
        service.runClear();
        assertEquals(0, GAME_DATABASE.getGamesList().size());
        assertNull(AUTH_DATABASE.findAuth(authToken));
        assertNull(USER_DATABASE.findUser(username));
    }
}
