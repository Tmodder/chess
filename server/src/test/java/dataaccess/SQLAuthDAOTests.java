package dataaccess;

import model.Authtoken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest
{
    private final String username = "bob";
    private final String token = "!@#$";
    private final SQLAuthDAO dao = new SQLAuthDAO();

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
    void createAuthAddsToTable()
    {
       try
       {
           dao.createAuth(new Authtoken(token,username));
           assertEquals(dao.findAuth(token).username(),username);

       } catch (DataAccessException e) {
           throw new RuntimeException(e);
       }
    }

    @Test
    void createAuthWithNullAuthFails()
    {
        assertThrows(DataAccessException.class,() ->  dao.createAuth(new Authtoken(null,username)));

    }


    @Test
    void findAuthTwiceWorks()
    {
        try
        {
            dao.createAuth(new Authtoken(token,username));
            assertNotNull(dao.findAuth(token));
            assertNotNull(dao.findAuth(token));
        }
        catch (DataAccessException e)
        {
            throw(new RuntimeException(e));
        }
    }

    @Test
    void findAuthWithWrongTokenFails()
    {
        try {
            dao.createAuth(new Authtoken(token,username));
            assertThrows(DataAccessException.class,() -> dao.findAuth(" "));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }


    }

    @Test
    void deleteAuthRemovesTokenFromTable()
    {
        try {
            dao.createAuth(new Authtoken(token,username));
            assertNotNull(dao.findAuth(token));
            dao.deleteAuth(new Authtoken(token,username));
            assertThrows(DataAccessException.class,() -> dao.findAuth(token));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearDropsTable()
    {
        try
        {
            dao.createAuth(new Authtoken(token,username));
            dao.clear();
            assertThrows(DataAccessException.class,()->dao.findAuth(token));
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}