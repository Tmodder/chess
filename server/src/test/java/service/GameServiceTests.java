package service;

import requestandresult.CreateGameRequest;
import requestandresult.JoinGameRequest;
import requestandresult.ListGamesRequest;
import requestandresult.RegisterRequest;
import dataaccess.GameDAO;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    private static final GameDAO GAME_DATABASE = GameDAO.makeInstance();
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
        assertNotNull(GAME_DATABASE.getGame(result.gameID()));
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
        assertEquals("bob", GAME_DATABASE.getGame(gameId).whiteUsername());
    }

    @Test
    @Order(4)
    public void joinGameasWhiteAgainFails()
    {
        GAME_DATABASE.clear();
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
        GAME_DATABASE.clear();
        assertNotNull(GameService.listGames(new ListGamesRequest(authToken)));
        assertEquals(0, GAME_DATABASE.getGamesList().size());

    }

    @Test
    @Order(6)
    public void listGamesWithMutipleGames()
    {
        GAME_DATABASE.clear();
        var createGameResultOne = GameService.createGame(new CreateGameRequest(authToken,game1));
        var createGameResultTwo = GameService.createGame(new CreateGameRequest(authToken, game2));
        var gameOneData = GAME_DATABASE.getGame(createGameResultOne.gameID());
        var gameTwoData = GAME_DATABASE.getGame(createGameResultTwo.gameID());
        var listResult = GameService.listGames(new ListGamesRequest(authToken));
        assertEquals(2,listResult.games().size());
    }
}
