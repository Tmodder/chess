package service;

import requestandresult.CreateGameRequest;
import requestandresult.RegisterRequest;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class ClearServiceTest {
    private static final UserDAO USER_DATABASE = UserDAO.makeInstance();
    private static final AuthDAO AUTH_DATABASE = AuthDAO.makeInstance();
    private static final GameDAO GAME_DATABASE = GameDAO.makeInstance();
    static String authToken;
    static String username = "bob";
    @BeforeAll
    public static void fillDb()
    {
        USER_DATABASE.clear();
        AUTH_DATABASE.clear();
        GAME_DATABASE.clear();
        var registerRes = UserService.registerService(new RegisterRequest(username,"password","email"));
        authToken = registerRes.authToken();
        GameService.createGame(new CreateGameRequest(authToken,"bobs game"));
    }

    @Test
    public void clearFullDb()
    {
        ClearService.runClear();
        assertEquals(0, GAME_DATABASE.getGamesList().size());
        assertNull(AUTH_DATABASE.findAuth(authToken));
        assertNull(USER_DATABASE.findUser(username));
    }
}
