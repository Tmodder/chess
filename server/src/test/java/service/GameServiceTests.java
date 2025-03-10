package service;

import dataaccess.*;
import requestandresult.CreateGameRequest;
import requestandresult.JoinGameRequest;
import requestandresult.ListGamesRequest;
import requestandresult.RegisterRequest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    private final AuthDAO AUTH_DATABASE = new MemoryAuthDAO();
    private final UserDAO USER_DATABASE = new MemoryUserDAO();
    private final UserService user = new UserService(USER_DATABASE, AUTH_DATABASE);
    private final GameDAO GAME_DATABASE = new MemoryGameDAO();
    private final GameService service = new GameService(AUTH_DATABASE,GAME_DATABASE);
    private final String game1 = "game1";
    private final String game2 = "game2";

    public String makeUser()
    {
        final String  username = "bob";
        final String  password = "password";
        final String  email = "bob@bob.com";
        var result = user.registerService(new RegisterRequest(username,password,email));
        return result.authToken();
    }


    @Test
    @Order(1)
    public void createGameAddsToDb()
    {
        var authToken = makeUser();
        var result = service.createGame(new CreateGameRequest(authToken,game1));
        try
        {
            assertNotNull(GAME_DATABASE.getGame(result.gameID()));
        }
        catch (DataAccessException e)
        {
           throw new ServiceError(e.getMessage());
        }

    }

    @Test
    @Order(2)
    public void createGameFailsWithNoAuth()
    {
        var authToken = makeUser();
        var result = service.createGame(new CreateGameRequest(authToken,game1));
        assertThrows(ServiceError.class, () -> service.createGame(new CreateGameRequest("bad",game1)));
    }

    @Test
    @Order(3)
    public void joinGameAsWhiteUpdatesGame()
    {
        try
        {
            AUTH_DATABASE.clear();
            USER_DATABASE.clear();
            var authToken = makeUser();
            var createGameResult = service.createGame(new CreateGameRequest(authToken,game1));
            int gameId = createGameResult.gameID();
            var joinGameResult = service.joinGame(new JoinGameRequest(authToken,"WHITE",gameId));
            assertEquals("bob", GAME_DATABASE.getGame(gameId).whiteUsername());
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Test
    @Order(4)
    public void joinGameasWhiteAgainFails()
    {
        try
        {
            AUTH_DATABASE.clear();
            USER_DATABASE.clear();
            var authToken = makeUser();
            GAME_DATABASE.clear();
            var createGameResult = service.createGame(new CreateGameRequest(authToken,game1));
            int gameId = createGameResult.gameID();
            var joinGameReq = new JoinGameRequest(authToken,"WHITE",gameId);
            var joinGameResultOne = service.joinGame(joinGameReq);
            assertThrows(ServiceError.class, () -> service.joinGame(joinGameReq));
        }
        catch (DataAccessException e)
        {
           throw new  ServiceError(e.getMessage());
        }

    }

    @Test
    @Order(5)
    public void listGamesWithNoGames()
    {
        try
        {
            AUTH_DATABASE.clear();
            USER_DATABASE.clear();
            var authToken = makeUser();
            GAME_DATABASE.clear();
            assertNotNull(service.listGames(new ListGamesRequest(authToken)));
            assertEquals(0, GAME_DATABASE.getGamesList().size());
        }
        catch (DataAccessException e)
        {
            throw new  ServiceError(e.getMessage());
        }


    }

    @Test
    @Order(6)
    public void listGamesWithMutipleGames()
    {
        try
        {
            AUTH_DATABASE.clear();
            USER_DATABASE.clear();
            var authToken = makeUser();
            GAME_DATABASE.clear();
            var createGameResultOne = service.createGame(new CreateGameRequest(authToken,game1));
            var createGameResultTwo = service.createGame(new CreateGameRequest(authToken, game2));
            var gameOneData = GAME_DATABASE.getGame(createGameResultOne.gameID());
            var gameTwoData = GAME_DATABASE.getGame(createGameResultTwo.gameID());
            var listResult = service.listGames(new ListGamesRequest(authToken));
            assertEquals(2,listResult.games().size());
        }
        catch (DataAccessException e)
        {
            throw new  ServiceError(e.getMessage());
        }

    }
}
