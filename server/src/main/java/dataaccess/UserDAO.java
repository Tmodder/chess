package dataaccess;
import model.User;

public interface UserDAO {

    void createUser(User newUser) throws DataAccessException;
    User findUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;

}
