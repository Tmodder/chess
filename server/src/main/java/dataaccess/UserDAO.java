package dataaccess;
import model.User;

public interface UserDAO {

    void createUser(String username, String password, String email);
    User findUser(String username);
    void deleteUser(String username)  throws DataAccessException;
}
