package dataaccess;

import model.User;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO
{
    @Override
    public void createUser(User newUser) throws DataAccessException{
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
            String insertUser = "INSERT INTO user_data VALUES (?,?,?)";
            try(var conn = DatabaseManager.getConnection())
            {
                var stmt = conn.prepareStatement(insertUser);
                stmt.setString(1, newUser.password());
                stmt.setString(2, newUser.username());
                stmt.setString(3, newUser.email());
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
            throw new DataAccessException(e.getMessage());
            }
    }

    @Override
    public User findUser(String username) throws DataAccessException
    {
        DatabaseManager.createTables();
        try(var conn = DatabaseManager.getConnection())
        {
            String selectUser = "SELECT * FROM user_data WHERE username = ?";
            var stmt = conn.prepareStatement(selectUser);
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            rs.next();
            return new User(rs.getString(1),rs.getString(2),rs.getString(3));

        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection())
        {
            try(var stmt = conn.createStatement())
            {
                stmt.executeUpdate("DROP TABLE IF EXISTS user_data");
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }

    }
}
