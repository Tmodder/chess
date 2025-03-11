package dataaccess;

import chess.ChessGame;
import model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTests
{
    private final SQLGameDAO dao = new SQLGameDAO();
    private final String whiteUsername = "Bob";
    private final String blackUsername = "Joe";
    private final String gameName = "game";
    private final ChessGame game = new ChessGame();


    @BeforeEach
    void setUp() {
        try
        {
            DatabaseManager.dropDatabase();
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }

    }
    @Test
    @Order(1)
    void createGameAddsGame() {
        try
        {
            int gameId = dao.createGame(new Game(-1,null,null,gameName,game));
            assertNotNull(dao.getGame(gameId));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }


    }

    @Test
    @Order(2)
    void createGameWithoutGamenameThrowsError()
    {
        assertThrows(DataAccessException.class, () -> dao.createGame(new Game(-1,null,null,null,game)));
    }
    @Test
    @Order(4)
    void getGameById() {
        try
        {
            int gameId = dao.createGame(new Game(-1,null,null,gameName,game));
            assertNotNull(dao.getGame(gameId));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void getGameWithBadIdThrowsError()
    {
        try
        {
            dao.createGame(new Game(-1,null,null,gameName,game));
            assertThrows(DataAccessException.class,() -> dao.getGame(0));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(6)
    void clearRemovesTables() {
        try
        {
            int gameId = dao.createGame(new Game(-1,null,null,gameName,game));
            dao.clear();
            assertThrows(DataAccessException.class, () -> dao.getGame(gameId));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Test
    @Order(7)
    void getGamesListWhileEmpty()
    {
        try
        {
            assertEquals(dao.getGamesList().size(),0);
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    void getGamesListWithGame()
    {
        try
        {
            dao.createGame(new Game(-1,null,null,gameName,game));
            assertNotNull(dao.getGamesList());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @Order(9)
    void addPlayerToGame()
    {
        try
        {
            int gameId = dao.createGame(new Game(-1,null,null,gameName,game));
            dao.addPlayerToGame("WHITE",whiteUsername,new Game(gameId, null,null,gameName,game));
            assertEquals(dao.getGame(gameId).whiteUsername(),whiteUsername);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    void addPlayerToFullGameThrowsError()
    {
        try
        {
            int gameId = dao.createGame(new Game(-1,null,null,gameName,game));
            dao.addPlayerToGame("WHITE",whiteUsername,dao.getGame(gameId));
            assertThrows(DataAccessException.class,() -> dao.addPlayerToGame("WHITE",whiteUsername,dao.getGame(gameId)));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}