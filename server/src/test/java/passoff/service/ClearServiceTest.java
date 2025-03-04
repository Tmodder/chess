package passoff.service;

import RequestResult.CreateGameRequest;
import RequestResult.RegisterRequest;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

import service.ClearService;
import service.GameService;
import service.UserService;

public class ClearServiceTest {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();
    private static final GameDAO gameDatabase = GameDAO.makeInstance();
    static String authToken;
    static String username = "bob";
    @BeforeAll
    public static void fillDb()
    {
        var registerRes = UserService.registerService(new RegisterRequest(username,"password","email"));
        authToken = registerRes.authToken();
        GameService.createGame(new CreateGameRequest(authToken,"bobs game"));
    }
    @Test

    public void clearFullDb()
    {
        ClearService.runClear();
        assertEquals(0,gameDatabase.getGamesList().size());
        assertNull(authDatabase.findAuth(authToken));
        assertNull(userDatabase.findUser(username));
    }
}
