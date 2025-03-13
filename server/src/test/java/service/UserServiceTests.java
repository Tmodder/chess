package service;
import dataaccess.*;
import org.junit.jupiter.api.*;
import requestandresult.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private final AuthDAO authDatabase = new MemoryAuthDAO();
    private final UserDAO userDatabase = new MemoryUserDAO();
    private final UserService service = new UserService(userDatabase, authDatabase);
    private final String username = "bob";
    private final String password = "1234";
    private final String email = "bob@bob.com";

    @BeforeEach
    void setUp()
    {
        service.registerService(new RegisterRequest(username,password,email));
    }

    @Test
    @Order(1)
    public void registerAddsUserToDb()
    {
        assertDoesNotThrow(()->userDatabase.findUser(username));
    }

    @Test
    @Order(2)
    public void registerWontAddSameUserTwice()
    {
        assertThrows(ServiceError.class,() -> service.registerService(new RegisterRequest(username,password,email)));

    }

    @Test
    @Order(3)
    public void loginAddsUserToAuthDb()
    {
        var req = new LoginRequest(username, password);
        var result = service.loginService(req);
        assertDoesNotThrow(() -> authDatabase.findAuth(result.authToken()));
    }

    @Test
    @Order(4)
    public void loginWithBadPasswordFails()
    {
        var req = new LoginRequest(username, "bad");
        assertThrows(ServiceError.class,() -> service.loginService(req));
    }

    @Test
    @Order(5)
    public void logoutRemovesUserFromAuthDb() throws DataAccessException {
        var loginResult = service.loginService(new LoginRequest(username,password));
        service.logoutService(new LogoutRequest(loginResult.authToken()));
        assertNull(authDatabase.findAuth(loginResult.authToken()));
    }

    @Test
    @Order(6)
    public void logoutWithBadAuthFails()
    {
        assertThrows(ServiceError.class,() -> service.logoutService(new LogoutRequest("bad")));
    }
}
