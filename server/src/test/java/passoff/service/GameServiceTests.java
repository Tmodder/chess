package passoff.service;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GameServiceTests {

    @Test
    @Order(7)
    public void createGameAddsToDb()
    {

    }

    @Test
    @Order(8)
    public void createGameFailsWithNoAuth()
    {

    }

    @Test
    @Order(9)
    public void joinGameAsWhiteUpdatesGame()
    {

    }

    @Test
    @Order(10)
    public void joinGameasWhiteAgainFails()
    {

    }

    @Test
    @Order(11)
    public void listGamesWithNoGames()
    {

    }

    @Test
    @Order(12)
    public void listGamesWithMutipleGames()
    {

    }
}
