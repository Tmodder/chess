package dataaccess;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest
{
    private final String username ="bob";
    private final String password = "1234";
    private final String email = "bob@bob.com";
    private final SQLUserDAO dao = new SQLUserDAO();
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
    @Order(1)
    @Test
    void createUserAddsToTable() {
        try
        {
            dao.createUser(new User(username,password, email));
            assertNotNull(dao.findUser(username));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }

    }
    @Order(2)
    @Test
    void createUserWithoutPasswordFails()
    {
        assertThrows(DataAccessException.class,() ->  dao.createUser(new User(username,null, email)));
    }

    @Order(3)
    @Test
    void findUserTwiceWithUsername() {
        try
        {
            dao.createUser(new User(username,password, email));
            assertEquals(password,dao.findUser(username).password());
            assertEquals(password,dao.findUser(username).password());
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Order(4)
    @Test
    void findUserWithBadPasswordFails()
    {
        try
        {
            dao.createUser(new User(username,password, email));
            assertNull(dao.findUser(null));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Order(5)
    @Test
    void clearDropsTable() {
        try
        {
            dao.createUser(new User(username,password, email));
            dao.clear();
            assertNull(dao.findUser(username));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}