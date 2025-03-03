package passoff.service;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.*;
import service.*;
import RequestResult.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();


    @Test
    @Order(1)
    public void registerAddsUserToDb()
    {
        String username = "bob";
        String password = "1234";
        String email = "bob@bob.com";
        var result = UserService.registerService(new RegisterRequest(username,password,email));
        assertNotNull(userDatabase.findUser(username));
    }

    @Test
    @Order(2)
    public void registerWontAddSameUserTwice()
    {
        String username = "bob";
        String password = "1234";
        String email = "bob@bob.com";
        assertThrows()
    }

    @Test
    @Order(3)
    public void loginAddsUserToAuthDb()
    {

    }

    @Test
    @Order(4)
    public void loginTwiceFails()
    {

    }

    @Test
    @Order(5)
    public void LogoutRemovesUserFromAuthDb()
    {

    }

    @Test
    @Order(6)
    public void LogoutOnSameUserFails()
    {

    }
}
