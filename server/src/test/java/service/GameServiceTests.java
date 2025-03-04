package service;

import RequestResult.CreateGameRequest;
import RequestResult.JoinGameRequest;
import RequestResult.ListGamesRequest;
import RequestResult.RegisterRequest;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.ServiceError;
import service.UserService;

public class GameServiceTests {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();
    private static final GameDAO gameDatabase = GameDAO.makeInstance();
    private static String authToken;
    private final String game1 = "game1";
    private final String game2 = "game2";

    @BeforeAll
    public static void makeUser()
    {
        final String  username = "bob";
        final String  password = "password";
        final String  email = "bob@bob.com";
        var result = UserService.registerService(new RegisterRequest(username,password,email));
        authToken = result.authToken();
    }


    @Test
    @Order(1)
    public void createGameAddsToDb()
    {
        var result = GameService.createGame(new CreateGameRequest(authToken,game1));
        assertNotNull(gameDatabase.getGame(result.gameID()));
    }

    @Test
    @Order(2)
    public void createGameFailsWithNoAuth()
    {
        assertThrows(ServiceError.class, () -> GameService.createGame(new CreateGameRequest("bad",game1)));
    }

    @Test
    @Order(3)
    public void joinGameAsWhiteUpdatesGame()
    {
        var createGameResult = GameService.createGame(new CreateGameRequest(authToken,game1));
        int gameId = createGameResult.gameID();
        var joinGameResult = GameService.joinGame(new JoinGameRequest(authToken,"WHITE",gameId));
        assertEquals("bob",gameDatabase.getGame(gameId).whiteUsername());
    }

    @Test
    @Order(4)
    public void joinGameasWhiteAgainFails()
    {
        gameDatabase.clear();
        var createGameResult = GameService.createGame(new CreateGameRequest(authToken,game1));
        int gameId = createGameResult.gameID();
        var joinGameReq = new JoinGameRequest(authToken,"WHITE",gameId);
        var joinGameResultOne = GameService.joinGame(joinGameReq);
        assertThrows(ServiceError.class, () -> GameService.joinGame(joinGameReq));
    }

    @Test
    @Order(5)
    public void listGamesWithNoGames()
    {
        gameDatabase.clear();
        assertNotNull(GameService.listGames(new ListGamesRequest(authToken)));
        assertEquals(0, gameDatabase.getGamesList().size());

    }

    @Test
    @Order(6)
    public void listGamesWithMutipleGames()
    {
        gameDatabase.clear();
        var createGameResultOne = GameService.createGame(new CreateGameRequest(authToken,game1));
        var createGameResultTwo = GameService.createGame(new CreateGameRequest(authToken, game2));
        var gameOneData = gameDatabase.getGame(createGameResultOne.gameID());
        var gameTwoData = gameDatabase.getGame(createGameResultTwo.gameID());
        var listResult = GameService.listGames(new ListGamesRequest(authToken));
        assertEquals(2,listResult.games().size());
    }
}
