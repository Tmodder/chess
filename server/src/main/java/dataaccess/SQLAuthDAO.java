package dataaccess;

import model.Authtoken;
import model.User;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO
{
    @Override
    public void createAuth(Authtoken auth) throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
        String insertAuth = "INSERT INTO authentication_data VALUES (?,?)";
        try(var conn = DatabaseManager.getConnection())
        {
            var stmt = conn.prepareStatement(insertAuth);
            stmt.setString(1, auth.username());
            stmt.setString(2, auth.authToken());
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Authtoken findAuth(String authString) throws DataAccessException {
        DatabaseManager.createTables();
        try(var conn = DatabaseManager.getConnection())
        {
            String selectAuth = "SELECT * FROM authentication_data WHERE auth_token = ?";
            var stmt = conn.prepareStatement(selectAuth);
            stmt.setString(1, authString);
            var rs = stmt.executeQuery();
            rs.next();
            return new Authtoken(rs.getString(2),rs.getString(1));

        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(Authtoken authToken) throws DataAccessException {
        DatabaseManager.createTables();
        try(var conn = DatabaseManager.getConnection())
        {
            String deleteAuth = "DELETE FROM authentication_data WHERE auth_token = ?";
            var stmt = conn.prepareStatement(deleteAuth);
            stmt.setString(1,authToken.authToken());
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try(var conn = DatabaseManager.getConnection())
        {
            try(var stmt = conn.createStatement())
            {
                stmt.executeUpdate("DROP TABLE IF EXISTS authentication_data");
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }
}
