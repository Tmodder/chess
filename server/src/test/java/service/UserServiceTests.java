package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.User;
import service.ServiceError;
import org.junit.jupiter.api.*;
import service.UserService;
import RequestResult.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();
    private final String username = "bob";
    private final String password = "1234";
    private final String email = "bob@bob.com";

    @Test
    @Order(1)
    public void registerAddsUserToDb()
    {
        userDatabase.clear();
        authDatabase.clear();
        var result = UserService.registerService(new RegisterRequest(username,password,email));
        assertNotNull(userDatabase.findUser(username));
    }

    @Test
    @Order(2)
    public void registerWontAddSameUserTwice()
    {
        assertThrows(ServiceError.class,() -> {UserService.registerService(new RegisterRequest(username,password,email));});

    }

    @Test
    @Order(3)
    public void loginAddsUserToAuthDb()
    {
        userDatabase.clear();
        authDatabase.clear();
        var res = UserService.registerService(new RegisterRequest(username,password,email));
        var req = new LoginRequest(username, password);
        var result = UserService.loginService(req);
        assertNotNull(authDatabase.findAuth(result.authToken()));
    }

    @Test
    @Order(4)
    public void loginWithBadPasswordFails()
    {
        var req = new LoginRequest(username, "bad");
        assertThrows(ServiceError.class,() -> {UserService.loginService(req);});
    }

    @Test
    @Order(5)
    public void LogoutRemovesUserFromAuthDb() throws DataAccessException {

        var loginResult = UserService.loginService(new LoginRequest(username,password));
        UserService.logoutService(new LogoutRequest(loginResult.authToken()));
        assertNull(authDatabase.findAuth(loginResult.authToken()));
    }

    @Test
    @Order(6)
    public void LogoutWithBadAuthFails()
    {
        assertThrows(ServiceError.class,() -> {UserService.logoutService(new LogoutRequest("bad"));});
    }
}
