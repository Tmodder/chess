package client;

import communication.ResponseException;
import communication.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    String userOne = "bob";
    String passwordOne = "1234";
    String emailOne = "bob@bob.com";
    String userTwo = "steve";
    String passwordTwo = "4321";
    String emailTwo = "steve@steve.com";
    String gameOne = "bobs game";
    String gameOneClone = gameOne;
    String gameTwo = "steves game";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDb()
    {
        facade.clear();
    }

    @Test
    public void registerCreatesAndLoginsIn() {
        facade.register(userOne,passwordOne,emailOne);
        Assertions.assertDoesNotThrow(facade::logout);
    }

    @Test
    public void registerTwiceThrowsError()
    {
        facade.register(userOne,passwordOne,emailOne);
        Assertions.assertThrows(ResponseException.class,() -> facade.register(userOne,passwordOne,emailOne));
    }

    @Test
    public void loginAllowsLogout()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.logout();
        facade.login(userOne,passwordOne);
        Assertions.assertDoesNotThrow(facade::logout);
    }

    @Test
    public void loginWithBadPasswordFails()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.logout();
        Assertions.assertThrows(ResponseException.class,() ->facade.login(userOne,"test"));
    }

    @Test
    public void createGameAddsToGamesList()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.createGame(gameOne);
        Assertions.assertNotNull(facade.listGames());
    }

    @Test
    public void createGameWithSameNameStillAdds()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.createGame(gameOne);
        Assertions.assertDoesNotThrow(() -> facade.createGame(gameOneClone));
    }

    @Test
    public void listGamesShowsGames()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.createGame(gameOne);
        facade.createGame(gameTwo);
        Assertions.assertNotNull(facade.listGames());
    }

    @Test
    public void listGamesReturnsNullWithEmptyList()
    {
        facade.register(userOne,passwordOne,emailOne);
        Assertions.assertNull(facade.listGames());
    }

    @Test
    public void playGameJoinsGame()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.createGame(gameOne);
        facade.listGames();
        Assertions.assertDoesNotThrow(() -> facade.playGame(1,"WHITE"));
    }

    @Test
    public void playGameThrowsErrorWhenJoiningFullGame()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.createGame(gameOne);
        facade.listGames();
        facade.playGame(1,"WHITE");
        facade.logout();
        facade.register(userTwo,passwordTwo,emailTwo);
        facade.listGames();
        Assertions.assertThrows(ResponseException.class,() ->facade.playGame(1,"WHITE"));
    }

    @Test
    public void clearClearsAll()
    {
        facade.register(userOne,passwordOne,emailOne);
        facade.createGame(gameOne);
        facade.createGame(gameOneClone);
        facade.logout();
        facade.register(userTwo,passwordTwo,emailTwo);
        facade.createGame(gameTwo);
        facade.clear();
        Assertions.assertThrows(ResponseException.class, () -> facade.login(userOne,passwordOne));
        facade.register(userOne,passwordOne,emailOne);
        Assertions.assertNull(facade.listGames());
    }


}
